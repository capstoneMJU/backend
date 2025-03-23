package capstone.mju.backend.domain.openapi.service;

import capstone.mju.backend.domain.openapi.entity.FoodNutrition;
import capstone.mju.backend.domain.openapi.config.FoodProperties;
import capstone.mju.backend.domain.openapi.entity.repository.FoodNutritionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class FoodNutritionService {

    private final FoodNutritionRepository foodRepo;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final FoodProperties ticketProperties;

    public void fetchAndSaveFoodData() {
        int page = 1;
        boolean hasNext = true;

        while (hasNext) {
            UriComponents uri = UriComponentsBuilder
                    .fromHttpUrl(ticketProperties.getBaseUrl() + "/getFoodNtrCpntDbInq01")
                    .queryParam("serviceKey", ticketProperties.getApikey())
                    .queryParam("type", "json")
                    .queryParam("numOfRows", 100)
                    .queryParam("pageNo", page)
                    .build();

            String json = restTemplate.getForObject(uri.toUri(), String.class);

            try {
                JsonNode items = objectMapper.readTree(json)
                        .path("body").path("items").path("item");

                if (!items.isArray() || items.size() == 0) {
                    hasNext = false;
                    break;
                }

                Iterator<JsonNode> iterator = items.elements();
                while (iterator.hasNext()) {
                    JsonNode item = iterator.next();
                    FoodNutrition food = parseFoodItem(item);
                    foodRepo.save(food);
                }

                page++;

            } catch (Exception e) {
                throw new RuntimeException("API 응답 파싱 실패: " + e.getMessage());
            }
        }
    }

    private FoodNutrition parseFoodItem(JsonNode item) {
        FoodNutrition food = new FoodNutrition();
        food.setFoodNmKr(item.path("FOOD_NM_KR").asText(null));
        food.setFoodCd(item.path("FOOD_CD").asText(null));
        food.setItemReportNo(item.path("ITEM_REPORT_NO").asText(null));
        food.setAmtNum1(item.path("AMT_NUM1").asDouble(0));
        food.setAmtNum3(item.path("AMT_NUM3").asDouble(0));
        food.setAmtNum4(item.path("AMT_NUM4").asDouble(0));
        food.setAmtNum7(item.path("AMT_NUM7").asDouble(0));
        food.setAmtNum8(item.path("AMT_NUM8").asDouble(0));
        food.setAmtNum14(item.path("AMT_NUM14").asDouble(0));
        food.setAmtNum24(item.path("AMT_NUM24").asDouble(0));
        food.setAmtNum25(item.path("AMT_NUM25").asDouble(0));
        food.setAmtNum26(item.path("AMT_NUM26").asDouble(0));
        food.setAmtNum52(item.path("AMT_NUM52").asDouble(0));
        food.setAmtNum53(item.path("AMT_NUM53").asDouble(0));
        food.setAmtNum54(item.path("AMT_NUM54").asDouble(0));
        food.setAmtNum55(item.path("AMT_NUM55").asDouble(0));
        food.setAmtNum56(item.path("AMT_NUM56").asDouble(0));
        food.setAmtNum57(item.path("AMT_NUM57").asDouble(0));
        food.setAmtNum58(item.path("AMT_NUM58").asDouble(0));
        food.setAmtNum59(item.path("AMT_NUM59").asDouble(0));
        food.setAmtNum61(item.path("AMT_NUM61").asDouble(0));
        food.setFoodOrNm(item.path("FOOD_OR_NM").asText(null));
        food.setFoodCat1Cd(item.path("FOOD_CAT1_CD").asText(null));
        food.setFoodCat1Nm(item.path("FOOD_CAT1_NM").asText(null));
        food.setFoodRefCd(item.path("FOOD_REF_CD").asText(null));
        food.setFoodRefNm(item.path("FOOD_REF_NM").asText(null));

        return food;
    }
}