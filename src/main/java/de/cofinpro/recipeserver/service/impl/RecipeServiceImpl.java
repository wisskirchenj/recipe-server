package de.cofinpro.recipeserver.service.impl;

import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.repository.RecipeRepository;
import de.cofinpro.recipeserver.service.RecipeService;
import de.cofinpro.recipeserver.service.exception.NotOwnerException;
import de.cofinpro.recipeserver.service.exception.RecipeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

/**
 * service layer class for serving "api/recipe" endpoints
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Recipe getById(long id) throws RecipeNotFoundException {
        return repository.findById(id).orElseThrow(() -> createNotFoundException(id));
    }

    @Override
    public List<Recipe> searchByCategory(String searchText) {
        return repository.findAllByCategoryEqualsIgnoreCaseOrderByDateTimeDesc(searchText);
    }

    @Override
    public List<Recipe> searchByName(String searchText) {
        return repository.findAllByNameContainsIgnoreCaseOrderByDateTimeDesc(searchText);
    }

    @Override
    public Recipe add(Recipe recipe) {
        return repository.save(recipe);
    }

    @Override
    public void delete(long id, String username) throws RecipeNotFoundException, NotOwnerException {
        handleCrudAction(id, username, repository::delete);
    }

    @Override
    public void update(long id, Recipe updateRecipe, String username)
            throws RecipeNotFoundException, NotOwnerException  {
        handleCrudAction(id, username, getUpdateAction(updateRecipe));
    }

    private void handleCrudAction(long id, String username, Consumer<Recipe> crudAction) {
        repository.findById(id)
                .ifPresentOrElse(recipe -> executeIfOwnerOrThrow(recipe, crudAction, username),
                        () -> { throw createNotFoundException(id); });
    }

    private Consumer<Recipe> getUpdateAction(Recipe updateRecipe) {
        return recipe -> {
            updateRecipe.setId(recipe.getId());
            repository.save(updateRecipe);
        };
    }

    private void executeIfOwnerOrThrow(Recipe recipe, Consumer<Recipe> action, String username)
            throws NotOwnerException {
        if (recipe.getCreator().getUsername().equals(username)) {
            action.accept(recipe);
        } else {
            throw new NotOwnerException();
        }
    }

    private RecipeNotFoundException createNotFoundException(long id) {
        return new RecipeNotFoundException("recipe with id %d not found".formatted(id));
    }
}
