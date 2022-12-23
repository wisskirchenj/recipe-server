package de.cofinpro.recipeserver.service;

import de.cofinpro.recipeserver.entities.Recipe;

public interface RecipeService {
    Recipe getRecipe();

    Recipe setRecipe(Recipe recipe);
}
