package de.cofinpro.recipeserver.security;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = { "spring.datasource.url=jdbc:h2:file:./src/test/resources/test_sec_db"})
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
    HttpHeaders mockUserheader;
    static boolean mockUserIsSetup = false;

    @BeforeEach
    void setup() {
        recipe = new Recipe().setName("n").setCategory("c").setDescription("d").setIngredients(List.of("i"))
                .setDirections(List.of("D"));
        objectMapper = new ObjectMapper();
        if (!mockUserIsSetup) {
            setupMockUser();
        }
        mockUserheader = new HttpHeaders();
        mockUserheader.setBasicAuth("a@b.c", "secret__");
    }

    private void setupMockUser() {
        mockUserIsSetup = true;
        Assertions.assertDoesNotThrow(() ->
                registerService.registerUser(userMapper.toEntity(new UserDto("a@b.c", "secret__"))));
    }


    @Test
    void getRecipeAuthenticated() throws Exception {
        mockMvc.perform(post("/api/recipe/new")
                        .headers(mockUserheader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk());
    }

    @Test
    void getRecipeUnauthenticated() throws Exception {
        mockMvc.perform(post("/api/recipe/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isUnauthorized());
    }
}
