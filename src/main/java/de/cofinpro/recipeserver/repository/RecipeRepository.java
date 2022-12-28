package de.cofinpro.recipeserver.repository;

import de.cofinpro.recipeserver.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByCategoryEqualsIgnoreCaseOrderByDateTimeDesc(String search);

    List<Recipe> findAllByNameContainsIgnoreCaseOrderByDateTimeDesc(String search);
}
