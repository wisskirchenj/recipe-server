package de.cofinpro.recipeserver.service.impl;

import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.repository.RecipeRepository;
import de.cofinpro.recipeserver.service.RecipeService;
import de.cofinpro.recipeserver.web.exception.RecipeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Recipe getById(long id) throws RecipeNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("recipe with id %d not found"
                        .formatted(id)));
    }

    @Override
    public Recipe add(Recipe recipe) {
        return repository.save(recipe);
    }

    @Override
    public void delete(long id) throws RecipeNotFoundException {
        repository.findById(id)
                .ifPresentOrElse(repository::delete,
                        () -> {
                            throw new RecipeNotFoundException("recipe with id %d not found"
                                    .formatted(id));
                        });
    }
}
