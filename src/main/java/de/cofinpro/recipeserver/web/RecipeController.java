package de.cofinpro.recipeserver.web;

import de.cofinpro.recipeserver.service.RecipeService;
import de.cofinpro.recipeserver.web.dto.RecipeDto;
import de.cofinpro.recipeserver.web.mapper.RecipeMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/recipe")
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
    ResponseEntity<Map<String, Long>> addRecipe(@Valid @RequestBody RecipeDto recipeDto,
                                                       @AuthenticationPrincipal Jwt jwt) {
        var saved = service.add(mapper.toEntity(recipeDto, jwt.getSubject()));
        return ok(Collections.singletonMap("id", saved.getId()));
    }

    /**
     * returns the first recipe if it exists.
     * @return RecipeDto with 200 = OK if id is found, 404 else (RecipeNotFoundException).
     */
    @GetMapping
    ResponseEntity<List<RecipeDto>> getAllRecipes() {
        return ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    /**
     * returns the recipe with id given as Json, if it exists.
     * @param id the recipe id to find
     * @return RecipeDto with 200 = OK if id is found, 404 else (RecipeNotFoundException).
     */
    @GetMapping("{id}")
    ResponseEntity<RecipeDto> getRecipe(@PathVariable long id) {
        return ok(mapper.toDto(service.getById(id)));
    }

    /**
     * searches for recipes with either a category or name search string given as request parameter.
     * @param category optional request search parameter, used for category search if specified
     * @param name optional name search parameter, used for name search if specified
     * @return 200 with found Recipe[] or empty if no match, 400 if not exactly one of the given parameters is provided.
     */
    @GetMapping("search/")
    ResponseEntity<List<RecipeDto>> searchRecipes(@RequestParam Optional<String> category,
                                                  @RequestParam Optional<String> name) {
        if (category.isEmpty() == name.isEmpty()) {
            return badRequest().build();
        }
        var foundRecipes = category
                .map(service::searchByCategory)
                .or(() -> name.map(service::searchByName))
                .orElseThrow();
        return ok(foundRecipes.stream().map(mapper::toDto).toList());
    }

    /**
     * deletes the recipe with id given from the database if it exists.
     * @param id the recipe id to delete
     * @return 204 = NoContent if id is found, 403 if not creator, 404 else (RecipeNotFoundException).
     */
    @DeleteMapping("{id}")
    ResponseEntity<Void> deleteRecipe(@PathVariable long id, @AuthenticationPrincipal Jwt jwt) {
        service.delete(id, jwt.getSubject());
        return noContent().build();
    }

    /**
     * deletes the recipe with di given from the database if it exists.
     * @param id the recipe id to delete
     * @return 204 = NoContent if id is found, 403 if not creator, 404 else (RecipeNotFoundException).
     */
    @PutMapping("{id}")
    ResponseEntity<Void> updateRecipe(@PathVariable long id,
                                             @Valid @RequestBody RecipeDto recipeDto,
                                             @AuthenticationPrincipal Jwt jwt) {
        service.update(id, mapper.toEntity(recipeDto, jwt.getSubject()), jwt.getSubject());
        return noContent().build();
    }
}
