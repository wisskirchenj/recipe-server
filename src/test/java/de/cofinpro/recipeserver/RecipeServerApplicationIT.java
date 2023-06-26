package de.cofinpro.recipeserver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.service.RegisterService;
import de.cofinpro.recipeserver.web.dto.RecipeDto;
import de.cofinpro.recipeserver.web.dto.UserDto;
import de.cofinpro.recipeserver.web.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(properties = { "spring.datasource.url=jdbc:postgresql://localhost:5432/recipetest"})
@AutoConfigureMockMvc
class RecipeServerApplicationIT {

    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    RegisterService registerService;
    @Autowired
    UserMapper userMapper;

    ObjectMapper objectMapper;
    HttpHeaders mockUserheader;
    static boolean mockUserIsSetup = false;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        if (!mockUserIsSetup) {
            setupMockUser();
        }
        mockUserheader = new HttpHeaders();
        mockUserheader.setBasicAuth("a@b.test", "secret__");
    }

    @Test
    void contextLoads() {
        assertNotNull(webApplicationContext.getBean("recipeController"));
    }

    @Test
    void whenPostRecipe_OkReturned() throws Exception {
        Recipe recipe = new Recipe().setName("n").setCategory("z").setDescription("d").setIngredients(List.of("i"))
                .setDirections(List.of("dir"));
        mockMvc.perform(post("/api/recipe/new")
                        .headers(mockUserheader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(recipe)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(longs = {77, 0 , -22})
    void whenGetWithNonExistingRecipeId_404Returned(long id) throws Exception {
        mockMvc.perform(get("/api/recipe/" + id)
                        .headers(mockUserheader))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"eins", "2.0", "3l", ".9"})
    void whenGetWithInvalidFormatRecipeId_400Returned(String pathVar) throws Exception {
        mockMvc.perform(get("/api/recipe/" + pathVar)
                        .headers(mockUserheader))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPostAndGetRecipe_OkReturnedWithRecipe() throws Exception {
        Recipe recipe = new Recipe().setName("name").setCategory("test").setDescription("descr").setIngredients(List.of("sth"))
                .setDirections(List.of("do it"));
        String recipeJson = objectMapper.writeValueAsString(recipe);
        long postId = postNew(recipeJson);
        var getResult = mockMvc.perform(get("/api/recipe/" + postId)
                        .headers(mockUserheader))
                .andExpect(status().isOk())
                .andExpect(content().json(recipeJson))
                .andReturn();
        System.out.println(getResult.getResponse().getContentAsString());
    }

    @Test
    void whenPutRecipe_204ReturnedIfExists404Or400Else() throws Exception {
        Recipe recipe = new Recipe().setName("name").setCategory("test").setDescription("descr").setIngredients(List.of("sth"))
                .setDirections(List.of("do it"));
        long postId = postNew(objectMapper.writeValueAsString(recipe));
        var recipeDto = new RecipeDto("n", "c", null, "d",
                List.of("i"), List.of("D"));
        mockMvc.perform(put("/api/recipe/" + postId)
                .headers(mockUserheader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto))
        ).andExpect(status().isNoContent());
        mockMvc.perform(put("/api/recipe/77")
                .headers(mockUserheader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto))
        ).andExpect(status().isNotFound());
        recipeDto = new RecipeDto("n", " ", null, "d",
                List.of("i"), List.of("D"));
        mockMvc.perform(put("/api/recipe/" + postId)
                .headers(mockUserheader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void whenDeleteRecipe_204ReturnedIfExists404Else() throws Exception {
        Recipe recipe = new Recipe().setName("n").setCategory("z").setDescription("d").setIngredients(List.of("i"))
                .setDirections(List.of("dir"));
        long postId = postNew(objectMapper.writeValueAsString(recipe));
        mockMvc.perform(delete("/api/recipe/" + postId)
                        .headers(mockUserheader))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/recipe/" + postId)
                        .headers(mockUserheader))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPostedTestRecipes_SearchByParametersGivesCorrectResults() throws Exception {
        var testRecipes = objectMapper.readValue(
                new ClassPathResource("json/test_recipes.json").getInputStream(),
                Recipe[].class
        );
        for (var recipe: testRecipes) {
            mockMvc.perform(post("/api/recipe/new")
                    .headers(mockUserheader)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(recipe)));
        }
        var expectedCategorySearchResult = Files.readString(
                new ClassPathResource("json/search_cat_vegetables.json").getFile().toPath());
        mockMvc.perform(get("/api/recipe/search/?category=VEGETABLES")
                        .headers(mockUserheader))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedCategorySearchResult));
        var expectedNameSearchResult = Files.readString(
                new ClassPathResource("json/search_name_EAS.json").getFile().toPath());
        mockMvc.perform(get("/api/recipe/search/?name=EaS")
                        .headers(mockUserheader))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedNameSearchResult));
    }

    @ParameterizedTest
    @ValueSource(strings = {"?categor=VEGETABLES", "?category=VEGETABLES&name=as", ""})
    void whenSearchByNoOrTwoParameters_400Returned(String searchPath) throws Exception {
        mockMvc.perform(get("/api/recipe/search/" + searchPath)
                        .headers(mockUserheader))
                .andExpect(status().isBadRequest());
    }


    private long postNew(String recipeJson) throws Exception {
        var postResult = mockMvc.perform(post("/api/recipe/new")
                        .headers(mockUserheader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recipeJson))
                .andReturn();
        return objectMapper.readValue(postResult.getResponse().getContentAsString(),
                new TypeReference<Map<String, Long>>(){}).get("id");
    }

    void setupMockUser() {
        mockUserIsSetup = true;
        Assertions.assertDoesNotThrow(() ->
                registerService.registerUser(userMapper.toEntity(new UserDto("a@b.test", "secret__"))));
    }
}
