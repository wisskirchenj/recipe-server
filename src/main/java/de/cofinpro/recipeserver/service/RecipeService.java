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

    /**
     * save given Recipe to the repository.
     * @return saved recipe enriched with id.
     */
    Recipe add(Recipe recipe);

    /**
     * delete recipe with given id if it exists
     * @param id of recipe to delete
     * @throws RecipeNotFoundException if no recipe with id is found
     */
    void delete(long id) throws RecipeNotFoundException;
}
