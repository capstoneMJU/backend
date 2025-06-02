package capstone.mju.backend.domain.ocr.service;

import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.CustomException;
import capstone.mju.backend.domain.common.exception.ForbiddenException;
import capstone.mju.backend.domain.common.exception.NotFoundException;
import capstone.mju.backend.domain.nutrition.dto.res.ScrapExistRes;
import capstone.mju.backend.domain.nutrition.entity.FoodNutrition;
import capstone.mju.backend.domain.nutrition.entity.repository.FoodNutritionRepository;
import capstone.mju.backend.domain.ocr.dto.response.FoodIdRes;
import capstone.mju.backend.domain.ocr.dto.response.OcrScrapRes;
import capstone.mju.backend.domain.ocr.entity.OcrScrap;
import capstone.mju.backend.domain.ocr.entity.repository.OcrScrapRepository;
import capstone.mju.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .orElseThrow(() -> new NotFoundException(ErrorCode.FOOD_NOT_FOUND));

        boolean alreadyExists = ocrScrapRepository.existsByUserAndFoodNutrition(user, food);
        if (alreadyExists) {
            throw new ForbiddenException(ErrorCode.ALREADY_SCRAPPED);
        }

        OcrScrap scrap = OcrScrap.builder()
                .user(user)
                .foodNutrition(food)
                .build();

        return ocrScrapRepository.save(scrap).getId();

    }

    public OcrScrapRes getScrapPage(User user, Pageable pageable) {
        Page<OcrScrap> pageResult = ocrScrapRepository.findAllByUser(user, pageable);

        List<FoodIdRes> content = pageResult.stream()
                .map(scrap -> FoodIdRes.builder()
                        .foodId(scrap.getFoodNutrition().getId())
                        .foodNmKr(scrap.getFoodNutrition().getFoodNmKr())
                        .build())
                .toList();

        return OcrScrapRes.builder()
                .content(content)
                .totalPages(pageResult.getTotalPages())
                .totalElements(pageResult.getTotalElements())
                .build();

    }
    @Transactional
    public void deleteScrap(User user, Long foodId) {
        FoodNutrition food = foodNutritionRepository.findById(foodId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.FOOD_NOT_FOUND));

        OcrScrap scrap = ocrScrapRepository.findByUserAndFoodNutrition(user, food)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SCRAP_NOT_FOUND));

        ocrScrapRepository.delete(scrap);
    }

    public ScrapExistRes existsScrap(User user, Long foodId) {
        FoodNutrition food = foodNutritionRepository.findById(foodId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.FOOD_NOT_FOUND));

        boolean exists = ocrScrapRepository.existsByUserAndFoodNutrition(user, food);

        return ScrapExistRes.builder()
                .scrapped(exists)
                .build();
    }
}
