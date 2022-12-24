package de.cofinpro.recipeserver.repository;

import de.cofinpro.recipeserver.entities.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeRepositoryTest {

    RecipeRepository repository;

    Recipe test;

    @BeforeEach
    void setUp() {
        repository = new RecipeRepository();
        test = new Recipe().setName("test").setDescription("description");
    }

    @Test
    void whenSetRecipe_GetReturnsValue() {
        var saved = repository.save(test);
        assertEquals(test.getName(), saved.getName());
        assertEquals(test.getDescription(), saved.getDescription());
        assertEquals(test.getIngredients(), saved.getIngredients());
        assertEquals(test.getDirections(), saved.getDirections());
        assertEquals(saved, repository.findById(saved.getId()).orElseThrow());
    }


    @Test
    void whenSetRecipeWithOtherValue_GetReturnsOtherValue() {
        Recipe first = new Recipe().setName("first");
        var saved = repository.save(first);
        assertEquals(first.getName(), saved.getName());
        assertEquals(saved, repository.findById(saved.getId()).orElseThrow());
        Recipe other = new Recipe().setName("other");
        var otherSaved = repository.save(other);
        assertEquals(other.getName(), otherSaved.getName());
        assertEquals(otherSaved, repository.findById(otherSaved.getId()).orElseThrow());
        assertTrue(otherSaved.getId() > saved.getId());
    }
}