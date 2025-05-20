package capstone.mju.backend.domain.nutrition.service;

import capstone.mju.backend.domain.nutrition.entity.FoodNutrition;
import capstone.mju.backend.domain.nutrition.config.FoodProperties;
import capstone.mju.backend.domain.nutrition.entity.repository.FoodNutritionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodNutritionInsertService {

    private final FoodNutritionRepository foodRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final FoodProperties foodProperties;

    public void fetchAndSaveFoodData() {
        int page = 1;
        boolean hasNext = true;

        // ① 기존 데이터의 itemReportNo 전부를 미리 메모리로 불러오기
        Set<String> existingItemNos = new HashSet<>(foodRepository.findAllItemReportNos());

        while (hasNext) {
            String baseUrl = foodProperties.getBaseUrl();
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }

            String fullUrl = baseUrl + "/getFoodNtrCpntDbInq02";

            UriComponents uri = UriComponentsBuilder
                    .fromHttpUrl(fullUrl)
                    .queryParam("serviceKey", foodProperties.getApikey())
                    .queryParam("type", "json")
                    .queryParam("pageNo", page)
                    .queryParam("numOfRows", 100)
                    .build(false);

            log.info("호출 URL = {}", uri.toUriString());

            String json = restTemplate.getForObject(uri.toUri(), String.class);

            try {
                log.debug("응답 내용 = {}", json.substring(0, Math.min(json.length(), 500)));

                JsonNode root = objectMapper.readTree(json);
                JsonNode header = root.path("header");
                String resultCode = header.path("resultCode").asText();
                String resultMsg = header.path("resultMsg").asText();

                log.info("resultCode = {}", resultCode);
                log.info("resultMsg = {}", resultMsg);

                JsonNode items = root.path("body").path("items");

                if (!items.isArray() || items.size() == 0) {
                    hasNext = false;
                    break;
                }

                for (JsonNode item : items) {
                    String itemReportNo = item.path("ITEM_REPORT_NO").asText(null);

                    // ② null 이거나 이미 저장된 번호라면 건너뛰기
                    if (itemReportNo == null || existingItemNos.contains(itemReportNo)) {
                        continue;
                    }

                    // ③ 저장
                    FoodNutrition food = parseFoodItem(item);
                    foodRepository.save(food);
                    existingItemNos.add(itemReportNo); // ④ 중복 방지를 위해 Set에도 추가
                    log.debug("저장됨: {}", food.getFoodNmKr());
                }

                log.info("현재 페이지: {}", page);
                page++;

            } catch (Exception e) {
                log.error("API 응답 파싱 실패", e);
                throw new RuntimeException("API 응답 파싱 실패: " + e.getMessage(), e);
            }
        }
    }




    private FoodNutrition parseFoodItem(JsonNode item) {
        FoodNutrition food = new FoodNutrition();

        food.setFoodNmKr(item.path("FOOD_NM_KR").asText(null));
        food.setFoodCd(item.path("FOOD_CD").asText(null));
        food.setItemReportNo(item.path("ITEM_REPORT_NO").asText(null));
        food.setAmtNum1(item.path("AMT_NUM1").asDouble(0));  // 에너지
        food.setAmtNum3(item.path("AMT_NUM3").asDouble(0));  // 단백질
        food.setAmtNum4(item.path("AMT_NUM4").asDouble(0));  // 지방
        food.setAmtNum6(item.path("AMT_NUM6").asDouble(0));  // 탄수화물
        food.setAmtNum7(item.path("AMT_NUM7").asDouble(0));  // 당류
        food.setAmtNum13(item.path("AMT_NUM13").asDouble(0));  // 나트륨
        food.setAmtNum23(item.path("AMT_NUM23").asDouble(0));  // 콜레스테롤
        food.setAmtNum24(item.path("AMT_NUM24").asDouble(0));  // 포화지방산
        food.setAmtNum25(item.path("AMT_NUM25").asDouble(0));  // 트랜스지방산
        food.setAmtNum51(item.path("AMT_NUM51").asDouble(0));  // 갈락토오스
        food.setAmtNum52(item.path("AMT_NUM52").asDouble(0));  // 과당
        food.setAmtNum53(item.path("AMT_NUM53").asDouble(0));  // 당알콜
        food.setAmtNum54(item.path("AMT_NUM54").asDouble(0));  // 맥아당
        food.setAmtNum55(item.path("AMT_NUM55").asDouble(0));  // 알룰로오스
        food.setAmtNum56(item.path("AMT_NUM56").asDouble(0));  // 에리스리톨
        food.setAmtNum57(item.path("AMT_NUM57").asDouble(0));  // 유당
        food.setAmtNum58(item.path("AMT_NUM58").asDouble(0));  // 자당
        food.setAmtNum60(item.path("AMT_NUM60").asDouble(0));  // 포도당
        food.setFoodOrNm(item.path("FOOD_OR_NM").asText(null));  // 식품 기원명
        food.setFoodCat1Nm(item.path("FOOD_CAT1_NM").asText(null));  // 식품 대분류명
        food.setFoodRefNm(item.path("FOOD_REF_NM").asText(null));  // 대표 식품명
        food.setNutri_amount_serving(item.path("NUTRI_AMOUNT_SERVING").asText(null));  // 1회 섭취참고량
        food.setZ10500(item.path("Z10500").asText(null));  // 식품 중량
        food.setSERVING_SIZE(item.path("SERVING_SIZE").asText(null));  // 1회 섭취량 (추가된 필드)
        food.setMakerNm(item.path("MAKER_NM").asText()); // 회사명
        return food;
    }

}
