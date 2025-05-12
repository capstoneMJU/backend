package capstone.mju.backend.domain.ocr.entity.repository;

import capstone.mju.backend.domain.nutrition.entity.FoodNutrition;
import capstone.mju.backend.domain.ocr.entity.OcrScrap;
import capstone.mju.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OcrScrapRepository extends JpaRepository<OcrScrap, UUID> {
    boolean existsByUserAndFoodNutrition(User user, FoodNutrition food);
}
