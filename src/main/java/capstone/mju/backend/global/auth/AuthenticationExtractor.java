package com.example.winterdeom.global.auth;

import com.example.winterdeom.domain.common.error.ErrorCode;
import com.example.winterdeom.domain.common.exception.UnauthorizedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class AuthenticationExtractor {
    private static final String TOKEN_COOKIE_NAME = "AccessToken";

    public static String extractTokenFromRequest(final HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "쿠키가 존재하지 않습니다.");
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(value -> value != null && !value.isEmpty())
                .findFirst()
                .map(token -> {
                    try {
                        return JwtEncoder.decodeJwtBearerToken(token);
                    } catch (Exception e) {
                        throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "토큰 디코딩에 실패했습니다.");
                    }
                })
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_TOKEN, "로그인 여부를 확인해주세요."));
    }
}

