package de.cofinpro.recipeserver.service;

import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.web.exception.RecipeNotFoundException;

import java.util.List;

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

    /**
     * update recipe with given id if it exists to the new updateRecipeDto data given
     * @param id of recipe to update
     * @param updateRecipe recipe data, to which the recipe gets updated
     * @throws RecipeNotFoundException if no recipe with id is found
     */
    void update(long id, Recipe updateRecipe);

    /**
     * case-insensitive search for recipes with given category search text
     * @param searchText category to search for
     * @return list of matching Recipe's
     */
    List<Recipe> searchByCategory(String searchText);

    /**
     * case-insensitive search for recipes with given name search text
     * @param searchText name sub string to search for (contains)
     * @return list of matching Recipe's
     */
    List<Recipe> searchByName(String searchText);
}
