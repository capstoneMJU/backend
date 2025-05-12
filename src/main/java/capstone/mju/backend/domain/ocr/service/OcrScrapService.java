package capstone.mju.backend.domain.ocr.service;

import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.CustomException;
import capstone.mju.backend.domain.nutrition.entity.FoodNutrition;
import capstone.mju.backend.domain.nutrition.entity.repository.FoodNutritionRepository;
import capstone.mju.backend.domain.ocr.entity.OcrScrap;
import capstone.mju.backend.domain.ocr.entity.repository.OcrScrapRepository;
import capstone.mju.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OcrScrapService {
    private final FoodNutritionRepository foodNutritionRepository;
    private final OcrScrapRepository ocrScrapRepository;
    @Transactional
    public UUID scrap(User user, Long foodId) {
        FoodNutrition food = foodNutritionRepository.findById(foodId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        boolean alreadyExists = ocrScrapRepository.existsByUserAndFoodNutrition(user, food);
        if (alreadyExists) {
            throw new CustomException(ErrorCode.ALREADY_SCRAPPED);
        }

        OcrScrap scrap = OcrScrap.builder()
                .user(user)
                .foodNutrition(food)
                .build();

        return ocrScrapRepository.save(scrap).getId();

    }
}
