package capstone.mju.backend.domain.nutrition.config;

import capstone.mju.backend.domain.nutrition.service.FoodNutritionInsertService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FoodFetchScheduler {

    private final FoodNutritionInsertService foodService;

    @Scheduled(cron = "0 0 1 1 * *") // 초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7, 일요일=0 또는 7)
    public void scheduleFetch() {
        foodService.fetchAndSaveFoodData();
    }
}