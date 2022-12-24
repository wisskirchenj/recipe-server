package de.cofinpro.recipeserver.service;

import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.web.exception.RecipeNotFoundException;

public interface RecipeService {
    /**
     * find and retrieve recipe with given id
     * @return the found recipe
     * @throws RecipeNotFoundException if no recipe with id is found
     */
    Recipe getById(long id) throws RecipeNotFoundException;

    Recipe add(Recipe recipe);
}

