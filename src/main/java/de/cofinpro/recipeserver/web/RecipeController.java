package de.cofinpro.recipeserver.web;

import de.cofinpro.recipeserver.service.RecipeService;
import de.cofinpro.recipeserver.web.dto.RecipeDto;
import de.cofinpro.recipeserver.web.mapper.RecipeMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("api")
public class RecipeController {

    private final RecipeService service;
    private final RecipeMapper mapper;

    @Autowired
    public RecipeController(RecipeService service,
                            RecipeMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("recipe")
    public ResponseEntity<RecipeDto> getRecipe() {
        return ok(mapper.toDto(service.getRecipe()));
    }

    @PostMapping("recipe")
    public ResponseEntity<Void> setRecipe(@Valid @RequestBody RecipeDto recipeDto) {
        service.setRecipe(mapper.toEntity(recipeDto));
        return ok().build();
    }
}
