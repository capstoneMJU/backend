package capstone.mju.backend.domain.openapi.controller;

import capstone.mju.backend.domain.openapi.service.FoodNutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodNutritionService foodService;

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchData() {
        foodService.fetchAndSaveFoodData();
        return ResponseEntity.ok("데이터 수집 완료");
    }
}