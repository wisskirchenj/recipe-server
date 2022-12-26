package de.cofinpro.recipeserver.web;

import de.cofinpro.recipeserver.service.RecipeService;
import de.cofinpro.recipeserver.web.dto.RecipeDto;
import de.cofinpro.recipeserver.web.mapper.RecipeMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/recipe")
public class RecipeController {

    private final RecipeService service;
    private final RecipeMapper mapper;

    @Autowired
    public RecipeController(RecipeService service,
                            RecipeMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * persist the posted recipe, if valid. On hibernate errors return 400.
     * @param recipeDto Json with RecipeDto to persist
     * @return key-value object "id": id of newly created recipe (database)
     */
    @PostMapping("new")
    public ResponseEntity<Map<String, Long>> setRecipe(@Valid @RequestBody RecipeDto recipeDto) {
        var saved = service.add(mapper.toEntity(recipeDto));
        return ok(Collections.singletonMap("id", saved.getId()));
    }

    /**
     * returns the recipe with id given as Json, if it exosts.
     * @param id the recipe id to find
     * @return RecipeDto with 200 = OK if id is found, 404 else (RecipeNotFoundException).
     */
    @GetMapping("{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable long id) {
        return ok(mapper.toDto(service.getById(id)));
    }

    /**
     * deletes the recipe with di given from the database if it exists.
     * @param id the recipe id to delete
     * @return 204 = NoContent if id is found, 404 else (RecipeNotFoundException).
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable long id) {
        service.delete(id);
        return noContent().build();
    }
}
