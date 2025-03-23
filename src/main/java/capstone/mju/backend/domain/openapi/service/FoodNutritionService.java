package capstone.mju.backend.domain.openapi.service;

import capstone.mju.backend.domain.openapi.dto.res.FoodNameResponseDto;
import capstone.mju.backend.domain.openapi.entity.FoodNutrition;
import capstone.mju.backend.domain.openapi.entity.repository.FoodNutritionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodNutritionService {
    private final FoodNutritionRepository foodRepository;
    public List<FoodNameResponseDto> searchFoodNames(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<FoodNutrition> foods = foodRepository.findByFoodNmKrContaining(keyword, pageable);
        return foods.map(FoodNameResponseDto::fromEntity).getContent();
    }
}
