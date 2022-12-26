package de.cofinpro.recipeserver.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * immutable DTO carrier for recipes in REST - endpoints
 */
public record RecipeDto(@NotBlank String name,
                        @NotBlank String description,
                        @NotNull @Size(min = 1) List<String> ingredients,
                        @NotNull @Size(min = 1) List<String> directions
) { }
