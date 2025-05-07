package capstone.mju.backend.domain.recipe.domain;

import capstone.mju.backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "recipe")
public class Recipe extends BaseEntity {
    private String title;

    @ManyToMany
    @JoinTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> requiredIngredients;

    @Lob
    private String recipeContent;
}
