package de.cofinpro.recipeserver.service;

import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.repository.RecipeRepository;
import de.cofinpro.recipeserver.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    RecipeRepository recipeRepository;

    RecipeService recipeService;

    Recipe test;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeServiceImpl(recipeRepository);
        test = new Recipe().setName("test").setDescription("test description");
    }

    @Test
    void whenGetRecipe_RepositoryValueReturned() {
        when(recipeRepository.get()).thenReturn(test);
        assertEquals(test, recipeService.getRecipe());
    }

    @Test
    void whenSetRecipe_RepositorySaveCalledWithSameValue() {
        test.setIngredients("some thing");
        when(recipeRepository.save(test)).thenReturn(test);
        assertEquals(test, recipeService.setRecipe(test));
        verify(recipeRepository).save(test);
    }
}