package capstone.mju.backend.domain.ocr.service;

import capstone.mju.backend.domain.ocr.dto.request.ConfirmReq;
import capstone.mju.backend.domain.ocr.dto.response.ConfirmRes;
import capstone.mju.backend.domain.ocr.dto.response.ScanRes;
import capstone.mju.backend.domain.openapi.entity.FoodNutrition;
import capstone.mju.backend.domain.openapi.entity.repository.FoodNutritionRepository;
import capstone.mju.backend.domain.sugarsubstitute.domain.SugarSubstitute;
import capstone.mju.backend.domain.sugarsubstitute.domain.repository.SugarSubstituteRepository;
import capstone.mju.backend.domain.sugarsubstitute.dto.res.SugarSubstituteRes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClovaOcrService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SugarSubstituteRepository substituteRepository;
    private final FoodNutritionRepository foodNutritionRepository;

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

        // DB에서 품목번호 기준으로 제품 검색
        Pageable pageable = PageRequest.of(0, 10); // 첫 페이지, 10개
        Page<FoodNutrition> foods = foodNutritionRepository.findByItemReportNoContaining(itemReportNo, pageable);

        if (foods.isEmpty()) {
            throw new IllegalArgumentException("해당 품목번호의 제품을 찾을 수 없습니다.");
        }

        // 여러 개 있으면 첫 번째 꺼 사용
        FoodNutrition food = foods.getContent().get(0);

        // 최종 반환
        return new ScanRes(food.getFoodNmKr(), itemReportNo, ocrText);
    }

    /**
     * OCR로 인식된 전체 텍스트에서 감미료 이름(정식 이름 또는 alias 포함)이 존재하는지 검사하여,
     * 해당 감미료들의 상세 정보를 반환한다.
     */
    public ConfirmRes confirm(ConfirmReq req) {
        String ocrText = req.getOcrText();

        List<SugarSubstitute> allSubs = substituteRepository.findAll();

        List<SugarSubstituteRes> matched = allSubs.stream()
                .filter(sub -> {
                    // 정식 이름 포함 여부 확인
                    if (ocrText.contains(sub.getName())) return true;
                    // alias(유사어) 포함 여부 확인
                    if (sub.getAlias() != null) {
                        for (String alias : sub.getAlias().split(",")) {
                            if (ocrText.contains(alias.trim())) return true;
                        }
                    }
                    return false;
                })
                .map(sub -> new SugarSubstituteRes(
                        sub.getName(),
                        sub.getCategory(),
                        sub.getDescription(),
                        sub.getSideEffect(),
                        sub.getGiIndex(),
                        sub.getCalorie()
                ))
                .collect(Collectors.toList());

        return new ConfirmRes(matched);
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

    // 품목번호 추출
    private String extractItemReportNo(String ocrText) {
//        // 1차: 키워드 기반 추출
//        List<String> keywords = List.of("품목보고번호", "품목 번호", "식품보고번호", "목보고번호", "보고번호", "품목");
//        for (String keyword : keywords) {
//            int idx = ocrText.indexOf(keyword);
//            if (idx != -1) {
//                String after = ocrText.substring(idx + keyword.length());
//                String digits = after.replaceAll("[^0-9]", "");
//                if (digits.length() >= 13) {
//                    return digits.substring(0, Math.min(digits.length(), 14));
//                }
//            }
//        }

        // 2차: 정규식으로 13~15자리 숫자 중 첫 번째를 추출
        Pattern pattern = Pattern.compile("\\d{13,15}");
        Matcher matcher = pattern.matcher(ocrText);
        if (matcher.find()) {
            return matcher.group();
        }

        // 3차: 추출 실패
        return null;
    }
}