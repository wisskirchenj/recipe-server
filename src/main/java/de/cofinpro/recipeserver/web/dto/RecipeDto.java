package de.cofinpro.recipeserver.web.dto;

import java.util.List;

/**
 * immutable DTO carrier for recipes in REST - endpoints
 */
public record RecipeDto(String name,
                        String description,
                        List<String> ingredients,
                        List<String> directions
) { }
