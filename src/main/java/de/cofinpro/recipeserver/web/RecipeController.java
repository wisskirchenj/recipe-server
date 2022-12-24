package de.cofinpro.recipeserver.web;

import de.cofinpro.recipeserver.service.RecipeService;
import de.cofinpro.recipeserver.web.dto.RecipeDto;
import de.cofinpro.recipeserver.web.mapper.RecipeMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

import static org.springframework.http.ResponseEntity.*;

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

    @GetMapping("{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable long id) {
        return ok(mapper.toDto(service.getById(id)));
    }

    @PostMapping("new")
    public ResponseEntity<Map<String, Long>> setRecipe(@Valid @RequestBody RecipeDto recipeDto) {
        var saved = service.add(mapper.toEntity(recipeDto));
        return ok(Collections.singletonMap("id", saved.getId()));
    }
}
