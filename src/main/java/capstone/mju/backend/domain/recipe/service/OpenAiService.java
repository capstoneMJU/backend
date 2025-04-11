package capstone.mju.backend.domain.recipe.service;

import capstone.mju.backend.domain.recipe.dto.response.RecipeResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    @Value("${openai.api-key}")
    private String openAiApiKey;
    private final WebClient openAiWebClient;
    public RecipeResult createRecipePromptAndTitle(String ingredients) {
        String prompt = String.format(
                "%s를 사용해서 저칼로리 레시피를 만들어줘. " +
                        "요리 제목(title)을 먼저 한 줄로 써주고, " +
                        "그 다음에 요리 순서(steps)를 번호를 매겨 (1. 2. 3.) 단계별로 구체적으로 작성해줘. " +
                        "불필요한 설명 없이 'title:', 'steps:' 형식으로 깔끔하게 구분해서 작성해줘.",
                ingredients
        );

        String response = openAiWebClient.post()
                .uri("/v1/chat/completions")
                .header("Authorization", "Bearer " + openAiApiKey)
                .bodyValue(buildRequestBody(prompt))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return parseRecipeResultFromResponse(response);
    }


    private Map<String, Object> buildRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4");

        List<Map<String, String>> messages = List.of(
                Map.of(
                        "role", "user",
                        "content", prompt
                )
        );
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1500);

        return requestBody;
    }

    private RecipeResult parseRecipeResultFromResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            String content = root.path("choices").get(0).path("message").path("content").asText();

            String[] lines = content.split("\n", 2);
            String titleLine = lines[0];
            String steps = lines.length > 1 ? lines[1].trim() : "";

            String title = titleLine.replace("제목:", "").trim();

            return new RecipeResult(title, steps);
        } catch (Exception e) {
            throw new RuntimeException("OpenAI 응답 파싱 실패", e);
        }
    }
}
