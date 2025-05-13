package capstone.mju.backend.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient openAiWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.openai.com")
                .build();
    }
    @Bean
    @Qualifier("naverImageWebClient")
    public WebClient naverImageWebClient() {
        return WebClient.builder()
                .baseUrl("https://openapi.naver.com")
                .build();
    }
}
