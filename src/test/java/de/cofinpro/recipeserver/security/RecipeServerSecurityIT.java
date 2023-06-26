package de.cofinpro.recipeserver.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.service.RegisterService;
import de.cofinpro.recipeserver.web.dto.UserDto;
import de.cofinpro.recipeserver.web.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = { "spring.datasource.url=jdbc:postgresql://localhost:5432/recipetest"})
@AutoConfigureMockMvc
class RecipeServerSecurityIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    RegisterService registerService;
    @Autowired
    UserMapper userMapper;

    Recipe recipe;
    ObjectMapper objectMapper;
    UserDto mockUser;
    HttpHeaders mockUserheader;
    HttpHeaders otherUserheader;
    static boolean mockUserIsSetup = false;

    @BeforeEach
    void setup() {
        recipe = new Recipe().setName("n").setCategory("c").setDescription("d").setIngredients(List.of("i"))
                .setDirections(List.of("D"));
        objectMapper = new ObjectMapper();
        setupMockUsers();
    }

    @Test
    void whenFalseUrlAuthenticated_403ReturnedSinceDenied() throws Exception {
        Recipe recipe = new Recipe();
        mockMvc.perform(post("/api/recie/1")
                        .headers(mockUserheader)
                        .content(objectMapper.writeValueAsBytes(recipe)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenFalseUrlUnuthenticated_401Returned() throws Exception {
        mockMvc.perform(get("/api"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerUnauthenticatedValidJson_AddsUser() throws Exception {
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserDto("hans.wurst@xyz.de", "12345678"))))
                .andExpect(status().isOk());
    }

    @Test
    void registerUnauthenticatedExistingUser_Gives400() throws Exception {
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUnauthenticatedInvalidDto_Gives400() throws Exception {
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserDto("wrong", "1234"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPostRecipeUnauthenticated_401Returned() throws Exception {
        mockMvc.perform(post("/api/recipe/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenGetRecipeUnauthenticated_401Returned() throws Exception {
        mockMvc.perform(get("/api/recipe/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenPutRecipeUnauthenticated_401Returned() throws Exception {
        mockMvc.perform(put("/api/recipe/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenDeleteRecipeUnauthenticated_401Returned() throws Exception {
        mockMvc.perform(delete("/api/recipe/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenDeleteOrPutAuthenticatedNotCreator_403Returned() throws Exception {
        var result = mockMvc.perform(post("/api/recipe/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe))
                        .headers(mockUserheader))
                .andExpect(status().isOk())
                .andReturn();
        var postId = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<Map<String, Long>>(){}).get("id");
        mockMvc.perform(delete("/api/recipe/" + postId)
                        .headers(otherUserheader))
                .andExpect(status().isForbidden());
        mockMvc.perform(put("/api/recipe/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe))
                        .headers(otherUserheader))
                .andExpect(status().isForbidden());
    }

    void setupMockUsers() {
        mockUser = new UserDto("a@b.c", "secret__");
        if (!mockUserIsSetup) {
            mockUserIsSetup = true;
            Assertions.assertDoesNotThrow(() ->
                    registerService.registerUser(userMapper.toEntity(mockUser)));
            Assertions.assertDoesNotThrow(() ->
                    registerService.registerUser(userMapper.toEntity(new UserDto("oth@b.c", "NOsecret"))));
        }
        mockUserheader = new HttpHeaders();
        mockUserheader.setBasicAuth("a@b.c", "secret__");
        otherUserheader = new HttpHeaders();
        otherUserheader.setBasicAuth("oth@b.c", "NOsecret");
    }
}
