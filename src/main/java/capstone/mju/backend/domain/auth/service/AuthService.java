package capstone.mju.backend.domain.auth.service;

import capstone.mju.backend.domain.auth.dto.request.JoinDto;
import capstone.mju.backend.domain.auth.repository.AuthRepository;
import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.ConflictException;
import capstone.mju.backend.domain.common.exception.NotFoundException;
import capstone.mju.backend.domain.common.exception.UnauthorizedException;
import capstone.mju.backend.domain.user.domain.User;
import capstone.mju.backend.domain.user.dto.request.LoginDto;
import capstone.mju.backend.global.auth.JwtEncoder;
import capstone.mju.backend.global.auth.JwtTokenProvider;
import capstone.mju.backend.global.auth.PasswordHashEncryption;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordHashEncryption passwordHashEncryption;
    private final JwtTokenProvider jwtTokenProvider;

    /*
    회원가입
     */
    public void join(JoinDto joinDto, HttpServletResponse response) {
        // 이메일이 이미 존재하는지 확인
        this.isEmailExist(joinDto.getEmail());
        String encryptedPassword = this.passwordHashEncryption.encrypt(joinDto.getPassword());

        // 이메일이 존재하지 않는다면 새로운 User 생성
        User user = User.builder()
                .email(joinDto.getEmail())
                .password(encryptedPassword)
                .username(joinDto.getUsername())
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
        log.info("login 진입");
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
                .secure(true)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
