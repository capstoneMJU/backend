package capstone.mju.backend.domain.openapi.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "api")
public class FoodProperties {
    @NotEmpty
    private String baseUrl;

    @NotEmpty
    private String apikey;
}