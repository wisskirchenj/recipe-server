package de.cofinpro.recipeserver.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * entity class (will be @Entity in further stages).
 */
@Data
@Accessors(chain = true)
public class Recipe {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long id;
    private String name;
    private String description;
    private List<String> ingredients;
    private List<String> directions;
}
