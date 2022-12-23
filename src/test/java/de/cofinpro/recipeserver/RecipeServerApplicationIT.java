package de.cofinpro.recipeserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.recipeserver.entities.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

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
        mockMvc.perform(post("/api/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipe)))
                .andExpect(status().isOk());
    }

    @Test
    void whenFalseUrl_404Returned() throws Exception {
        Recipe recipe = new Recipe();
        mockMvc.perform(post("/api")
                        .content(objectMapper.writeValueAsBytes(recipe)))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/api"))
                .andExpect(status().isNotFound());
    }


    @Test
    void whenPostAndGetRecipe_OkReturnedWithRecipe() throws Exception {
        Recipe recipe = new Recipe().setName("name").setIngredients("sth");
        String recipeJson = objectMapper.writeValueAsString(recipe);
        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recipeJson))
                .andExpect(status().isOk());
        var result = mockMvc.perform(get("/api/recipe"))
                .andExpect(status().isOk())
                .andExpect(content().json(recipeJson))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

}
