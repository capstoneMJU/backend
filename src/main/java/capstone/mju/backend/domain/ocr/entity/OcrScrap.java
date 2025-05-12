package capstone.mju.backend.domain.ocr.entity;

import capstone.mju.backend.domain.common.BaseEntity;
import capstone.mju.backend.domain.nutrition.entity.FoodNutrition;
import capstone.mju.backend.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ocr_scrap",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "food_nutrition_id"}))
public class OcrScrap extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_nutrition_id", nullable = false)
    private FoodNutrition foodNutrition;
}