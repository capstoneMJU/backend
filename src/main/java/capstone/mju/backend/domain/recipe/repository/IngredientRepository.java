package capstone.mju.backend.domain.recipe.repository;

import capstone.mju.backend.domain.recipe.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
}
