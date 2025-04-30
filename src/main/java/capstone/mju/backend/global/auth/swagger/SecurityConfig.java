package capstone.mju.backend.global.auth.swagger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/user/**", "/auth/join").permitAll()
                        .requestMatchers("/api/v1/news/**").permitAll()
                        .requestMatchers("/api/v1/foods/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/boards").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/boards/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/boards/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/boards/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/comments/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/comments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/comments/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/comments/**").permitAll()
                        .requestMatchers("api/v1/likes/**").permitAll()
                        .requestMatchers("/posts/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(config -> config.disable())
                .httpBasic(config -> config.disable());

        return http.build();
    }
}