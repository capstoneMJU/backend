package capstone.mju.backend.domain.ocr.service;

import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.CustomException;
import capstone.mju.backend.domain.nutrition.entity.FoodNutrition;
import capstone.mju.backend.domain.nutrition.entity.repository.FoodNutritionRepository;
import capstone.mju.backend.domain.ocr.dto.request.ConfirmReq;
import capstone.mju.backend.domain.ocr.dto.response.ConfirmRes;
import capstone.mju.backend.domain.ocr.dto.response.ScanRes;
import capstone.mju.backend.domain.sugarsubstitute.entity.FoodNutritionSweetener;
import capstone.mju.backend.domain.sugarsubstitute.entity.SugarSubstitute;
import capstone.mju.backend.domain.sugarsubstitute.entity.repository.FoodNutritionSweetenerRepository;
import capstone.mju.backend.domain.sugarsubstitute.entity.repository.SugarSubstituteRepository;
import capstone.mju.backend.domain.sugarsubstitute.dto.res.SugarSubstituteRes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ClovaOcrService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SugarSubstituteRepository substituteRepository;
    private final FoodNutritionRepository foodNutritionRepository;
    private final FoodNutritionSweetenerRepository foodNutritionSweetenerRepository;

    @Value("${clova.ocr.api-url}")
    private String apiUrl;

    @Value("${clova.ocr.secret-key}")
    private String secretKey;

    /**
     * 이미지 파일을 OCR로 분석하여
     * 1. 텍스트 추출
     * 2. 품목번호 추출
     * 3. DB에서 제품명, 대체당 정보 찾아서 반환
     */

    // 사진 스캔 및 품목번호, 제품명 추출
    public ScanRes scan(File imageFile) throws Exception {
        String json = callOcrApi(imageFile);
        JsonNode root = objectMapper.readTree(json);
        JsonNode fields = root.at("/images/0/fields");

        if (!fields.isArray()) {
            throw new IllegalStateException("OCR 결과에 fields 정보가 없습니다.");
        }

        // OCR 전체 텍스트 합치기
        StringBuilder fullTextBuilder = new StringBuilder();
        for (JsonNode field : fields) {
            String text = field.get("inferText").asText();
            fullTextBuilder.append(text).append(" ");
        }
        String ocrText = fullTextBuilder.toString().trim();

        // 품목번호 추출
        String itemReportNo = extractItemReportNo(ocrText);

        // 디버깅 로그
        System.out.println("OCR 전체 텍스트 =====================");
        System.out.println(ocrText);
        System.out.println("=======================================");
        System.out.println("추출된 itemReportNo: [" + itemReportNo + "]");

        if (itemReportNo == null) {
            throw new IllegalArgumentException("품목번호를 인식할 수 없습니다.");
        }

        // 영양정보 엔티티 조회
        FoodNutrition food = getExactFoodByItemReportNo(itemReportNo);

        ScanRes res = ScanRes.builder()
                .foodNmKr(food.getFoodNmKr())
                .itemReportNo(itemReportNo)
                .makerNm(food.getMakerNm())
                .ocrText(ocrText)
                .build();

        return res;
    }

    /**
     * OCR로 인식된 전체 텍스트에서 감미료 이름(정식 이름 또는 alias 포함)이 존재하는지 검사하여,
     * 해당 감미료들의 상세 정보를 반환한다.
     */
    public ConfirmRes confirm(ConfirmReq req) {
        String ocrText = req.getOcrText();
        String itemReportNo = req.getItemReportNo();

        // 영양정보 엔티티 조회
        FoodNutrition food = getExactFoodByItemReportNo(itemReportNo);

        // 전체 대체당 불러오기
        List<SugarSubstitute> allSubs = substituteRepository.findAll();

        // 매치된 대체당 찾기 및 저장
        List<SugarSubstituteRes> matched = new ArrayList<>();

        for (SugarSubstitute sub : allSubs) {
            boolean matchedName = ocrText.contains(sub.getName());
            boolean matchedAlias = sub.getAlias() != null &&
                    Arrays.stream(sub.getAlias().split(","))
                            .anyMatch(alias -> ocrText.contains(alias.trim()));

            if (matchedName || matchedAlias) {
                // 매핑 정보 반환용
                matched.add(SugarSubstituteRes.builder()
                        .name(sub.getName())
                        .category(sub.getCategory().getKoreanName())
                        .description(sub.getDescription())
                        .sideEffect(sub.getSideEffect())
                        .giIndex(sub.getGiIndex())
                        .calorie(sub.getCalorie())
                        .build());

                // 중복 매핑 방지 후 저장
                boolean alreadyExists = foodNutritionSweetenerRepository.existsByFoodNutritionAndSugarSubstitute(food, sub);

                if (!alreadyExists) {
                    FoodNutritionSweetener mapping = FoodNutritionSweetener.builder()
                                    .foodNutrition(food)
                                    .sugarSubstitute(sub)
                                    .build();
                    foodNutritionSweetenerRepository.save(mapping);
                }
            }
        }

        ConfirmRes res = ConfirmRes.builder()
                .matchedSubstitutes(matched)
                .build();

        return res;
    }


    /**
     *CLOVA OCR API 호출 - 이미지 파일을 base64로 인코딩해서 요청
     */
    private String callOcrApi(File file) throws Exception {
        byte[] imageBytes = Files.readAllBytes(file.toPath());
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        Map<String, Object> requestJson = new HashMap<>();
        requestJson.put("version", "V2");
        requestJson.put("requestId", UUID.randomUUID().toString());
        requestJson.put("timestamp", System.currentTimeMillis());

        Map<String, Object> image = new HashMap<>();
        image.put("format", "png");
        image.put("name", "demo");
        image.put("data", base64Image);

        requestJson.put("images", List.of(image));

        StringEntity entity = new StringEntity(objectMapper.writeValueAsString(requestJson), ContentType.APPLICATION_JSON);

        HttpPost post = new HttpPost(apiUrl);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("X-OCR-SECRET", secretKey);
        post.setEntity(entity);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpResponse response = client.execute(post);
            return EntityUtils.toString(response.getEntity());
        }
    }

    /**
     *OCR 이미지에서 품목번호 추출
     */
    private String extractItemReportNo(String ocrText) {
        // 정규식으로 12~16자리 숫자 중 첫 번째를 추출
        Pattern pattern = Pattern.compile("\\d{12,16}");
        Matcher matcher = pattern.matcher(ocrText);
        if (matcher.find()) {
            return matcher.group();
        }

        return null;
    }

    /**
     * DB에서 품목번호 기준으로 제품 검색
     */
    private FoodNutrition getExactFoodByItemReportNo(String itemReportNo) {
        validateSearchKeyword(itemReportNo);

        List<FoodNutrition> foods = foodNutritionRepository.findByItemReportNoContaining(itemReportNo.trim());

        return foods.stream()
                .filter(food -> {
                    String[] codes = food.getItemReportNo().split("/");
                    for (String code : codes) {
                        if (code.trim().equals(itemReportNo.trim())) {
                            return true;
                        }
                    }
                    return false;
                })
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
    }
    private void validateSearchKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_SEARCH_KEYWORD);
        }
    }


}