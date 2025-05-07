package capstone.mju.backend.domain.recipe.repository;

import capstone.mju.backend.domain.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
    Recipe findByTitle(String title);
}
