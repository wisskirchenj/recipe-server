package de.cofinpro.recipeserver.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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
    RecipeRepository recipeRepository;

    @MockBean
    JwtDecoder jwtDecoder;

    Recipe recipe;
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        recipe = new Recipe().setName("n").setCategory("c").setDescription("d").setIngredients(List.of("i"))
                .setDirections(List.of("D"));
        objectMapper = new ObjectMapper();
    }

    @Test
    void whenFalseUrlAuthenticated_403ReturnedSinceDenied() throws Exception {
        Recipe recipe = new Recipe();
        mockMvc.perform(post("/api/recie/1")
                        .with(jwt())
                        .content(objectMapper.writeValueAsBytes(recipe)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenFalseUrlUnauthenticated_401Returned() throws Exception {
        mockMvc.perform(get("/api"))
                .andExpect(status().isUnauthorized());
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
                        .with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        var postId = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<Map<String, Long>>(){}).get("id");
        var changedOwner = recipeRepository.findById(postId).orElseThrow().setCreator("other");
        recipeRepository.save(changedOwner);
        mockMvc.perform(delete("/api/recipe/" + postId)
                        .with(jwt()))
                .andExpect(status().isForbidden());
        mockMvc.perform(put("/api/recipe/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe))
                        .with(jwt()))
                .andExpect(status().isForbidden());
    }
}
