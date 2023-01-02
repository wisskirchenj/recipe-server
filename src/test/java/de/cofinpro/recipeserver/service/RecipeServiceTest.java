package de.cofinpro.recipeserver.service;

import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.entities.User;
import de.cofinpro.recipeserver.repository.RecipeRepository;
import de.cofinpro.recipeserver.web.exception.NotOwnerException;
import de.cofinpro.recipeserver.web.exception.RecipeNotFoundException;
import de.cofinpro.recipeserver.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    RecipeRepository recipeRepository;

    RecipeService recipeService;

    Recipe test;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeServiceImpl(recipeRepository);
        test = new Recipe().setName("test").setDescription("test description").setId(2L);
    }

    @Test
    void whenGetRecipeValidId_RepositoryValueReturned() {
        when(recipeRepository.findById(2L)).thenReturn(Optional.of(test));
        Assertions.assertEquals(test, recipeService.getById(2L));
    }

    @Test
    void whenGetRecipeInvalidId_ExceptionThrown() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(RecipeNotFoundException.class, () -> recipeService.getById(1L));
    }
    @Test
    void whenSetRecipe_RepositorySaveCalledWithSameValue() {
        test.setIngredients(List.of("some thing"));
        when(recipeRepository.save(test)).thenReturn(test);
        Assertions.assertEquals(test, recipeService.add(test));
        verify(recipeRepository).save(test);
    }

    @Test
    void whenInvalidId_DeleteThrows() {
        when(recipeRepository.findById(2L)).thenReturn(Optional.empty());
        Assertions.assertThrows(RecipeNotFoundException.class, () -> recipeService.delete(2L, null));
        verify(recipeRepository).findById(2L);
        verify(recipeRepository, never()).delete(any(Recipe.class));
    }

    @Test
    void whenInvalidId_UpdateThrows() {
        when(recipeRepository.findById(2L)).thenReturn(Optional.empty());
        var updated = new Recipe();
        Assertions.assertThrows(RecipeNotFoundException.class, () -> recipeService.update(2L, updated, null));
        verify(recipeRepository).findById(2L);
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void whenNotOwner_DeleteThrowsNotOwner() {
        when(recipeRepository.findById(2L))
                .thenReturn(Optional.of(new Recipe().setCreator(new User().setUsername("owner"))));
        Assertions.assertThrows(NotOwnerException.class, () -> recipeService.delete(2L, "other"));
        verify(recipeRepository).findById(2L);
        verify(recipeRepository, never()).delete(any(Recipe.class));
    }

    @Test
    void whenOwner_DeleteDeletes() {
        var recipe = new Recipe().setCreator(new User().setUsername("owner"));
        when(recipeRepository.findById(2L)).thenReturn(Optional.of(recipe));
        recipeService.delete(2L, "owner");
        verify(recipeRepository).delete(recipe);
    }

    @Test
    void whenNotOwner_UpdateThrowsNotOwner() {
        when(recipeRepository.findById(2L))
                .thenReturn(Optional.of(new Recipe().setCreator(new User().setUsername("owner"))));
        Assertions.assertThrows(NotOwnerException.class, () -> recipeService.update(2L, null,"other"));
        verify(recipeRepository).findById(2L);
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void whenOwner_UpdateUpdates() {
        var recipe = new Recipe().setCreator(new User().setUsername("owner"));
        when(recipeRepository.findById(2L)).thenReturn(Optional.of(recipe));
        recipeService.update(2L, recipe.setCategory("cat"), "owner");
        verify(recipeRepository).save(recipe);
    }
}