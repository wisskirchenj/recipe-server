package de.cofinpro.recipeserver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.recipeserver.entities.Recipe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class RecipeServerApplicationIT {

    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void contextLoads() {
        assertNotNull(webApplicationContext.getBean("recipeController"));
    }

    @Test
    void whenPostRecipe_OkReturned() throws Exception {
        Recipe recipe = new Recipe();
        mockMvc.perform(post("/api/recipe/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipe)))
                .andExpect(status().isOk());
    }

    @Test
    void whenFalseUrl_404Returned() throws Exception {
        Recipe recipe = new Recipe();
        mockMvc.perform(post("/api/recipe")
                        .content(objectMapper.writeValueAsBytes(recipe)))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/api"))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(longs = {77, 0 , -22})
    void whenGetWithNonExistingRecipeId_404Returned(long id) throws Exception {
        mockMvc.perform(get("/api/recipe/" + id))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"eins", "2.0", "3l", ".9"})
    void whenGetWithInvalidFormatRecipeId_400Returned(String pathVar) throws Exception {
        mockMvc.perform(get("/api/recipe/" + pathVar))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPostAndGetRecipe_OkReturnedWithRecipe() throws Exception {
        Recipe recipe = new Recipe().setName("name").setIngredients(List.of("sth"));
        String recipeJson = objectMapper.writeValueAsString(recipe);
        System.out.println(recipeJson);
        var postResult = mockMvc.perform(post("/api/recipe/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recipeJson))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id\":")))
                .andReturn();
        var postId = objectMapper.readValue(postResult.getResponse().getContentAsString(),
                new TypeReference<Map<String, Long>>(){}).get("id");
        var getResult = mockMvc.perform(get("/api/recipe/" + postId))
                .andExpect(status().isOk())
                .andExpect(content().json(recipeJson))
                .andReturn();
        System.out.println(getResult.getResponse().getContentAsString());
    }

}
