package de.cofinpro.recipeserver.web.mapper;

import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.entities.User;
import de.cofinpro.recipeserver.web.dto.RecipeDto;
import org.springframework.stereotype.Component;

/**
 * Manually implemented entity <-> dto mapper.
 */
@Component
public class RecipeMapper {

    public RecipeDto toDto(Recipe recipe) {
        return new RecipeDto(recipe.getName(), recipe.getCategory(), recipe.getDateTime(),
                recipe.getDescription(), recipe.getIngredients(), recipe.getDirections());
    }

    public Recipe toEntity(RecipeDto recipeDto, User user) {
        return new Recipe().setName(recipeDto.name())
                .setCategory(recipeDto.category())
                .setDescription(recipeDto.description())
                .setIngredients(recipeDto.ingredients())
                .setDirections(recipeDto.directions())
                .setCreator(user);
    }
}
