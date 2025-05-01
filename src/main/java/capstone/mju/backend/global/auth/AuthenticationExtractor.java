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
        log.info("토큰 추출 시도: 요청에서 쿠키 확인");

        if (request.getCookies() == null) {
            log.info("요청에 쿠키가 존재하지 않습니다.");
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "쿠키가 존재하지 않습니다.");
        }

        return Arrays.stream(request.getCookies())
                .peek(cookie -> log.info("쿠키 확인 - 이름: {}, 값: {}", cookie.getName(), cookie.getValue()))
                .filter(cookie -> TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(value -> value != null && !value.isEmpty())
                .findFirst()
                .map(token -> {
                    try {
                        log.info("AccessToken 쿠키 발견. 토큰 디코딩 시도: {}", token);
                        return JwtEncoder.decodeJwtBearerToken(token);
                    } catch (Exception e) {
                        log.info("토큰 디코딩 실패: {}", e.getMessage());
                        throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "토큰 디코딩에 실패했습니다.");
                    }
                })
                .orElseThrow(() -> {
                    log.info("AccessToken 쿠키가 없거나 값이 비어 있습니다.");
                    return new UnauthorizedException(ErrorCode.INVALID_TOKEN, "로그인 여부를 확인해주세요.");
                });
    }
}
