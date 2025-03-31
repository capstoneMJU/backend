package capstone.mju.backend.domain.news.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "naver.news")
public class NewsProperties {
    private String clientId;
    private String clientSecret;
    private String url;

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
