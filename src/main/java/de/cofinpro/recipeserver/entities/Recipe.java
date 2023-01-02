package de.cofinpro.recipeserver.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * entity class for the recipes.
 */
@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain = true)
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String category;
    @UpdateTimestamp
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime dateTime;
    private String description;
    @ElementCollection
    private List<String> ingredients;
    @ElementCollection
    private List<String> directions;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne(optional = false)
    private User creator;
}