package de.cofinpro.recipeserver.service.impl;

import de.cofinpro.recipeserver.entities.Recipe;
import de.cofinpro.recipeserver.repository.RecipeRepository;
import de.cofinpro.recipeserver.service.RecipeService;
import de.cofinpro.recipeserver.service.exception.NotOwnerException;
import de.cofinpro.recipeserver.service.exception.RecipeNotFoundException;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

import static java.time.LocalDate.now;

/**
 * service layer class for serving "api/recipe" endpoints
 */
@Service
@Slf4j
@RegisterReflectionForBinding({org.hibernate.generator.internal.CurrentTimestampGeneration.class})
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;
    private final ObservationRegistry observationRegistry;

    @Autowired
    public RecipeServiceImpl(RecipeRepository repository,
                             ObservationRegistry observationRegistry) {
        this.repository = repository;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public Recipe getById(long id) throws RecipeNotFoundException {
        return repository.findById(id).orElseThrow(() -> createNotFoundException(id));
    }

    @Override
    public List<Recipe> getAll() {
        return repository.findAll();
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
        if (recipe.getCreator().equals(username)) {
            action.accept(recipe);
        } else {
            throw new NotOwnerException();
        }
    }

    private RecipeNotFoundException createNotFoundException(long id) {
        Observation.createNotStarted("recipe.not.found", this.observationRegistry)
                .contextualName("recipe.not.found")
                .lowCardinalityKeyValue("missedIdAtDate", id + "@" + now())
                .start();
        return new RecipeNotFoundException("recipe with id %d not found".formatted(id));
    }
}
