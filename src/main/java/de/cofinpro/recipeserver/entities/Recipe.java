package de.cofinpro.recipeserver.entities;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Recipe {
    private String name;
    private String description;
    private String ingredients;
    private String directions;
}
