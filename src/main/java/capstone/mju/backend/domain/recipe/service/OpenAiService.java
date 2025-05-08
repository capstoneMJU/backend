package capstone.mju.backend.domain.recipe.service;

import capstone.mju.backend.domain.recipe.domain.Ingredient;
import capstone.mju.backend.domain.recipe.dto.response.RecipeDetailResponse;
import capstone.mju.backend.domain.recipe.dto.response.RecipeSuggestionResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAiService {
    @Value("${openai.api-key}")
    private String openAiApiKey;

    private final WebClient openAiWebClient;

    @Value("${openai.model}")
    private String openAiModel;

    @Value("${openai.temperature}")
    private double temperature;

    @Value("${openai.max-tokens}")
    private int maxTokens;

    private final ObjectMapper objectMapper = new ObjectMapper();
    public RecipeDetailResponse createRecipePromptAndTitle(String title, String ingredients) {
        String prompt = String.format(
                "%s를 사용해서 저칼로리 레시피를 만들어줘. " +
                        "요리 제목(title)을 먼저 한 줄로 써주고, " +
                        "그 다음에 요리 순서(steps)를 번호를 매겨 (1. 2. 3.) 단계별로 구체적으로 작성해줘. " +
                        "불필요한 설명 없이 'title:','ingredients: ', 'steps:' 형식으로 깔끔하게 구분해서 작성해줘. " +
                        "레시피 제목은: '%s'",
                ingredients, title
        );

        String response = callOpenAi(prompt);
        return parseRecipeResultFromResponse(response, ingredients);
    }

    public List<RecipeSuggestionResponse> generateRecipeSuggestions(String ingredients) {
        String prompt = String.format("""
                    아래 재료를 사용해서 만들 수 있는 저칼로리 요리 3~5개를 추천해줘.
                    각 요리는 제목(title)과 필요한 재료들(ingredients)을 쉼표로 구분한 문자열로 제공해.
                    아래 JSON 형식으로 응답해줘:
                    [
                      {
                        "title": "요리 이름",
                        "ingredients": "재료1, 재료2, 재료3"
                      }
                    ]
                    사용자 재료: %s
                """, ingredients);

        String response = callOpenAi(prompt);
        return parseRecipeSuggestions(response);
    }


    private String callOpenAi(String prompt) {
        return openAiWebClient.post()
                .uri("/v1/chat/completions")
                .header("Authorization", "Bearer " + openAiApiKey)
                .bodyValue(buildRequestBody(prompt))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private Map<String, Object> buildRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", openAiModel);

        List<Map<String, String>> messages = List.of(
                Map.of("role", "user", "content", prompt)
        );
        requestBody.put("messages", messages);
        requestBody.put("temperature", temperature);
        requestBody.put("max_tokens", maxTokens);

        return requestBody;
    }

    public RecipeDetailResponse parseRecipeResultFromResponse(String response, String ingredients) {
        try {
            JsonNode root = objectMapper.readTree(response);
            String content = root.path("choices").get(0).path("message").path("content").asText();

            String[] parts = content.split("ingredients:", 2);
            String titleLine = parts[0].trim();
            String title = titleLine.replace("title:", "").trim();

            List<Ingredient> ingredientList = parseIngredients(ingredients);
            log.info("Ingredients: {}", ingredientList);

            String steps = parts[1].split("steps:", 2).length > 1 ? parts[1].split("steps:", 2)[1].trim() : "";

            return RecipeDetailResponse.of(title, ingredientList, steps);
        } catch (Exception e) {
            throw new RuntimeException("OpenAI 응답 파싱 실패", e);
        }
    }

    private List<Ingredient> parseIngredients(String ingredientsLine) {
        String[] ingredientsArray = ingredientsLine.split(",");
        List<Ingredient> ingredientList = Arrays.stream(ingredientsArray)
                .map(ingredient -> {
                    String cleanedIngredient = ingredient.replaceAll("^[0-9]+\\.", "").trim();
                    return new Ingredient(cleanedIngredient);
                })
                .collect(Collectors.toList());
        return ingredientList;
    }

    private List<RecipeSuggestionResponse> parseRecipeSuggestions(String response) {
        System.out.println("OpenAI 응답: " + response);
        try {
            System.out.println("Raw response: " + response);

            String content = objectMapper.readTree(response)
                    .path("choices").get(0)
                    .path("message")
                    .path("content").asText();

            if (content.isEmpty()) {
                throw new RuntimeException("응답 내용이 비어 있습니다.");
            }

            System.out.println("Parsed content: " + content);

            return objectMapper.readValue(content, new TypeReference<List<RecipeSuggestionResponse>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("레시피 추천 응답 파싱 실패. 응답 내용: " + response, e);
        }
    }
}