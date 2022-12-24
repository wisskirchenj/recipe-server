package de.cofinpro.recipeserver.repository;

import de.cofinpro.recipeserver.entities.Recipe;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RecipeRepository {

    private final List<Recipe> recipes = new ArrayList<>();

    /**
     * get Recipe with index (id - 1), i.e. starting from 1, if such an element is stored.
     * @param id the recipe id to find
     * @return Optional of the found recipe or Optional.empty() if given id is out of range
     */
    public Optional<Recipe> findById(long id) {
        if (id > 0 && id <= recipes.size()) {
            return Optional.of(recipes.get((int)id - 1));
        } else {
            return Optional.empty();
        }
    }

    /**
     * append a recipe to the recipes list-store and give it the id of the new list-size.
     * @param newRecipe recipe to save
     * @return the Recipe object given - enriched with the id (=list index) where it is stored
     */
    public Recipe save(Recipe newRecipe) {
        recipes.add(newRecipe);
        newRecipe.setId(recipes.size());
        return newRecipe;
    }
}
