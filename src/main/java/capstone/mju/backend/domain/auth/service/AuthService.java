package com.example.winterdeom.domain.auth.service;

import com.example.winterdeom.domain.auth.dto.request.LoginDto;
import com.example.winterdeom.domain.auth.repository.AuthRepository;
import com.example.winterdeom.domain.common.error.ErrorCode;
import com.example.winterdeom.domain.common.exception.ConflictException;
import com.example.winterdeom.domain.common.exception.NotFoundException;
import com.example.winterdeom.domain.common.exception.UnauthorizedException;
import com.example.winterdeom.domain.user.domain.User;
import com.example.winterdeom.global.auth.JwtEncoder;
import com.example.winterdeom.global.auth.JwtTokenProvider;
import com.example.winterdeom.global.auth.PasswordHashEncryption;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordHashEncryption passwordHashEncryption;
    private final JwtTokenProvider jwtTokenProvider;

    /*
    회원가입
     */
    public void join(LoginDto loginDto, HttpServletResponse response) {
        // 이메일이 이미 존재하는지 확인
        this.isEmailExist(loginDto.getEmail());
        String encryptedPassword = this.passwordHashEncryption.encrypt(loginDto.getPassword());

        // 이메일이 존재하지 않는다면 새로운 User 생성
        User user = User.builder()
                .email(loginDto.getEmail())
                .password(encryptedPassword)
                .build();

        authRepository.save(user);
    }

    /*
    Email 유일성 확인
     */
    public void isEmailExist(String email) {
        User user = this.authRepository.findByEmail(email);
        if (user != null) {
            throw new ConflictException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    /*
    login
     */
    public void login(LoginDto loginDto, HttpServletResponse response) {
        User user = this.authRepository.findByEmail(loginDto.getEmail());

        if(user == null) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        if (!passwordHashEncryption.matches(loginDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }

        String payload = user.getId().toString();
        String accessToken = jwtTokenProvider.createToken(payload);
        ResponseCookie cookie = ResponseCookie.from("AccessToken", JwtEncoder.encodeJwtBearerToken(accessToken))
                .maxAge(Duration.ofMillis(1800000))
                .httpOnly(true)
                .sameSite("None")
                .secure(false)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
