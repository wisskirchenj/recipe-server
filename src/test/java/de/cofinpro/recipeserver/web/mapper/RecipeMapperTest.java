package de.cofinpro.recipeserver.web.mapper;

import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.web.dto.RecipeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeMapperTest {

    Recipe recipe;
    RecipeDto recipeDto;

    RecipeMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new RecipeMapper();
        recipe = new Recipe().setName("n1").setDescription("d1").setIngredients("i1").setDirections("D1");
        recipeDto = new RecipeDto("n2", "d2", "i2", "D2");
    }

    @Test
    void toDto() {
        RecipeDto mapped = mapper.toDto(recipe);
        assertEquals(recipe.getName(), mapped.name());
        assertEquals(recipe.getDescription(), mapped.description());
        assertEquals(recipe.getIngredients(), mapped.ingredients());
        assertEquals(recipe.getDirections(), mapped.directions());
    }

    @Test
    void toEntity() {
        Recipe mapped = mapper.toEntity(recipeDto);
        assertEquals(recipeDto.name(), mapped.getName());
        assertEquals(recipeDto.description(), mapped.getDescription());
        assertEquals(recipeDto.ingredients(), mapped.getIngredients());
        assertEquals(recipeDto.directions(), mapped.getDirections());
    }
}