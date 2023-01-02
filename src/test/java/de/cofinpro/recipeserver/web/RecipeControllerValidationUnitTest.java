package de.cofinpro.recipeserver.web;

import de.cofinpro.recipeserver.web.dto.RecipeDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeControllerValidationUnitTest {

    JpaUnitTestValidator<RecipeDto> recipeValidator
            = new JpaUnitTestValidator<>(this::getValidRecipeRequest, RecipeDto.class);

    RecipeDto getValidRecipeRequest() {
        return new RecipeDto("Apple pie",
                "cake",
                LocalDateTime.of(2000, 6,15, 22, 0),
                "Easy to prepare apple pie",
                List.of("100g sugar", "100ml milk"),
                List.of("make dough", "bake")
        );
    }

    @ParameterizedTest
    @MethodSource
    void whenValidRecipeDto_NoError(String fieldName, Object validValue) throws Exception {
        assertTrue(recipeValidator.getConstraintViolationsOnUpdate(fieldName, validValue).isEmpty());
    }

    static Stream<Arguments> whenValidRecipeDto_NoError() {
        return Stream.of(
                Arguments.of("name", " some recipe !"),
                Arguments.of("name", "Name"),
                Arguments.of("name", "long\n 2 lines\n"),
                Arguments.of("category", " some cat !"),
                Arguments.of("category", "soup"),
                Arguments.of("category", "long\n 2 lines\n"),
                Arguments.of("date", LocalDateTime.MIN),
                Arguments.of("date", LocalDateTime.MAX),
                Arguments.of("date", null),
                Arguments.of("description", "  ?"),
                Arguments.of("description", "more\nthan\none\nline\n ?"),
                Arguments.of("ingredients", List.of("1")),
                Arguments.of("ingredients", List.of("","")),
                Arguments.of("directions", List.of("1","2"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void whenInvalidRecipe_DtoHasOneValidationError(String fieldName, Object invalidValue) throws Exception {
        assertEquals(1, recipeValidator.getConstraintViolationsOnUpdate(fieldName, invalidValue).size());
    }

    static Stream<Arguments> whenInvalidRecipe_DtoHasOneValidationError() {
        return Stream.of(
                Arguments.of("name", "   "),
                Arguments.of("name", ""),
                Arguments.of("name", "\t"),
                Arguments.of("name", null),
                Arguments.of("category", "   "),
                Arguments.of("category", ""),
                Arguments.of("category", "\t"),
                Arguments.of("category", null),
                Arguments.of("description", "   "),
                Arguments.of("description", ""),
                Arguments.of("description", "\t"),
                Arguments.of("description", null),
                Arguments.of("ingredients", List.of()),
                Arguments.of("ingredients", null),
                Arguments.of("directions", List.of()),
                Arguments.of("ingredients", null)
        );
    }
}