package de.cofinpro.recipeserver.web.mapper;

import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.entities.User;
import de.cofinpro.recipeserver.web.dto.RecipeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeMapperTest {

    Recipe recipe;
    RecipeDto recipeDto;

    RecipeMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new RecipeMapper();
        recipe = new Recipe().setName("n1").setCategory("c").setId(17).setDescription("d1")
                .setIngredients(List.of("i1", "i2")).setDirections(List.of("D1"));
        recipeDto = new RecipeDto("n2", "c", LocalDateTime.MIN, "d2",
                List.of("i2"), List.of("D2"));
    }

    @Test
    void toDto() {
        RecipeDto mapped = mapper.toDto(recipe);
        assertEquals(recipe.getName(), mapped.name());
        assertEquals(recipe.getCategory(), mapped.category());
        assertEquals(recipe.getDescription(), mapped.description());
        assertEquals(recipe.getIngredients(), mapped.ingredients());
        assertEquals(recipe.getDirections(), mapped.directions());
        assertNull(mapped.date());
    }

    @Test
    void toEntity() {
        User user = new User().setUsername("test");
        Recipe mapped = mapper.toEntity(recipeDto, user);
        assertEquals(recipeDto.name(), mapped.getName());
        assertEquals(recipeDto.category(), mapped.getCategory());
        assertEquals(recipeDto.description(), mapped.getDescription());
        assertEquals(recipeDto.ingredients(), mapped.getIngredients());
        assertEquals(recipeDto.directions(), mapped.getDirections());
        assertEquals(user, mapped.getCreator());
    }
}