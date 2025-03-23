package capstone.mju.backend.domain.openapi.config;

import capstone.mju.backend.domain.openapi.service.FoodNutritionInsertService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FoodFetchScheduler {

    private final FoodNutritionInsertService foodService;

    @Scheduled(cron = "0 0 3 1 * *") // 매월 1일 새벽 3시
    public void scheduleFetch() {
        foodService.fetchAndSaveFoodData();
    }
}