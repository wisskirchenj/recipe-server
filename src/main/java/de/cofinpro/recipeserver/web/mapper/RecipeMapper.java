package de.cofinpro.recipeserver.web.mapper;

import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.web.dto.RecipeDto;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper {

    public RecipeDto toDto(Recipe recipe) {
        return new RecipeDto(recipe.getName(), recipe.getDescription(), recipe.getIngredients(),
                recipe.getDirections());
    }

    public Recipe toEntity(RecipeDto recipeDto) {
        return new Recipe().setName(recipeDto.name()).setDescription(recipeDto.description())
                .setIngredients(recipeDto.ingredients()).setDirections(recipeDto.directions());
    }
}
