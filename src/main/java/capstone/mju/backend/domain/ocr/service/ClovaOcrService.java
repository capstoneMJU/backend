package capstone.mju.backend.domain.ocr.service;

import capstone.mju.backend.domain.ocr.dto.response.OcrIngredientsRes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClovaOcrService {

    @Value("${clova.ocr.api-url}")
    private String apiUrl;

    @Value("${clova.ocr.secret-key}")
    private String secretKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public OcrIngredientsRes extractData(File imageFile) throws Exception {
        String json = callOcrApi(imageFile);
        JsonNode root = objectMapper.readTree(json);
        JsonNode cells = root.at("/images/0/cells");

        if (!cells.isArray()) {
            throw new IllegalStateException("OCR 결과에 cells 정보가 없습니다.");
        }

        // 다양한 표현 대응 키워드
        List<String> ingredientKeywords = List.of("원재료명", "원재료명 및 함량", "성분", "원재료", "내용물");
        List<String> codeKeywords = List.of("품목보고번호", "품목 번호", "식품보고번호");

        // rowIndex별로 묶기
        Map<Integer, List<JsonNode>> rowMap = new HashMap<>();
        for (JsonNode cell : cells) {
            int rowIndex = cell.get("rowIndex").asInt();
            rowMap.computeIfAbsent(rowIndex, k -> new ArrayList<>()).add(cell);
        }

        String ingredients = null;
        String productCode = null;

        for (Map.Entry<Integer, List<JsonNode>> entry : rowMap.entrySet()) {
            int rowIndex = entry.getKey();
            List<JsonNode> row = entry.getValue();

            // 가로형 대응 (같은 줄에 키워드와 값이 나란히 있는 경우)
            if (row.size() >= 2) {
                String left = row.get(0).get("inferText").asText();
                String right = row.get(1).get("inferText").asText();

                if (ingredientKeywords.stream().anyMatch(keyword -> left.contains(keyword)) && ingredients == null) {
                    ingredients = right;
                }

                if (codeKeywords.stream().anyMatch(keyword -> left.contains(keyword)) && productCode == null) {
                    productCode = right.replaceAll("[^0-9]", "");
                }
            }

            // 세로형 대응 (키워드가 단독 줄에 있고, 다음 줄에 값이 있는 경우)
            for (JsonNode cell : row) {
                String text = cell.get("inferText").asText();

                if (ingredientKeywords.stream().anyMatch(keyword -> text.contains(keyword)) && ingredients == null) {
                    List<JsonNode> nextRow = rowMap.getOrDefault(rowIndex + 1, List.of());
                    ingredients = nextRow.stream()
                            .map(c -> c.get("inferText").asText())
                            .collect(Collectors.joining(" "));
                }

                if (codeKeywords.stream().anyMatch(keyword -> text.contains(keyword)) && productCode == null) {
                    List<JsonNode> nextRow = rowMap.getOrDefault(rowIndex + 1, List.of());
                    productCode = nextRow.stream()
                            .map(c -> c.get("inferText").asText())
                            .collect(Collectors.joining(" "))
                            .replaceAll("[^0-9]", "");
                }
            }
        }

        return new OcrIngredientsRes(ingredients, productCode);
    }

    private String callOcrApi(File file) throws Exception {
        byte[] imageBytes = Files.readAllBytes(file.toPath());
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        // Clova OCR API 요청 JSON 구성
        Map<String, Object> requestJson = new HashMap<>();
        requestJson.put("version", "V2");
        requestJson.put("requestId", UUID.randomUUID().toString());
        requestJson.put("timestamp", System.currentTimeMillis());

        Map<String, Object> image = new HashMap<>();
        image.put("format", "png");
        image.put("name", "demo");
        image.put("data", base64Image);
        image.put("table", true);

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
}