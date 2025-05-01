package capstone.mju.backend.global.auth;

import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.UnauthorizedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class AuthenticationExtractor {
    private static final String TOKEN_COOKIE_NAME = "AccessToken";

    public static String extractTokenFromRequest(final HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        log.info("[Auth] Authorization 헤더: {}", authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("[Auth] Authorization 헤더에서 토큰 추출 성공");
            return token;
        }

        if (request.getCookies() == null) {
            log.warn("[Auth] 쿠키 없음");
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "쿠키가 존재하지 않습니다.");
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(value -> value != null && !value.isEmpty())
                .map(token -> {
                    try {
                        // Bearer+ 접두사 제거
                        if (token.startsWith("Bearer+")) {
                            token = token.substring(7);
                        } else if (token.startsWith("Bearer ")) {
                            token = token.substring(7);
                        }
                        return JwtEncoder.decodeJwtBearerToken(token);
                    } catch (Exception e) {
                        log.error("[Auth] 토큰 디코딩 실패: {}", e.getMessage(), e);
                        throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "토큰 디코딩에 실패했습니다.");
                    }
                })
                .findFirst()
                .orElseThrow(() -> {
                    log.error("[Auth] AccessToken 쿠키 없음 또는 값 비어 있음");
                    return new UnauthorizedException(ErrorCode.INVALID_TOKEN, "로그인 여부를 확인해주세요.");
                });
    }
}

