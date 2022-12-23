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
        assertEquals(test, repository.save(test));
        assertEquals(test, repository.get());
    }


    @Test
    void whenSetRecipeWithOtherValue_GetReturnsOtherValue() {
        assertEquals(test, repository.save(test));
        assertEquals(test, repository.get());
        Recipe other = new Recipe().setName("other");
        repository.save(other);
        assertEquals(other, repository.get());
    }
}