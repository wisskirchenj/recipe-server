package de.cofinpro.recipeserver.repository;

import de.cofinpro.recipeserver.entities.Recipe;
import org.springframework.stereotype.Repository;

@Repository
public class RecipeRepository {

    private Recipe recipe = new Recipe();

    public Recipe get() {
        return recipe;
    }

    public Recipe save(Recipe newRecipe) {
        this.recipe = newRecipe;
        return recipe;
    }
}
