package capstone.mju.backend.domain.recipe.repository;

import capstone.mju.backend.domain.recipe.domain.Recipe;
import capstone.mju.backend.domain.recipe.domain.ScrapRecipe;
import capstone.mju.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScrapRecipeRepository extends JpaRepository<ScrapRecipe, UUID> {
    boolean existsByUserAndRecipe(User user, Recipe recipe);

    List<ScrapRecipe> findByUser(User user);

    void deleteByUserAndRecipe(User user, Recipe recipe);
}
