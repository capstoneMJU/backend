package capstone.mju.backend.domain.recipe.domain;

import capstone.mju.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "recipe")
public class Recipe extends BaseEntity {
    private String title;
    private List<Ingredient> requiredIngredients;
    private String recipeContent;
}
