package capstone.mju.backend.domain.user.controller;

import capstone.mju.backend.domain.auth.dto.request.JoinDto;
import capstone.mju.backend.domain.auth.service.AuthService;
import capstone.mju.backend.domain.common.ResponseDto;
import capstone.mju.backend.domain.user.dto.request.LoginDto;
import capstone.mju.backend.domain.user.dto.response.LoginData;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@Tag(name = "User API", description = "유저 관련 API")
public class UserController {
    private final AuthService authService;
    // 로그인
    @PostMapping("/user/login")
    public ResponseEntity<ResponseDto<LoginData>> login(@RequestBody LoginDto loginDto, HttpServletResponse request) {
        log.info("login controller 진입");
        LoginData loginData = authService.login(loginDto, request);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "login successfully", loginData), HttpStatus.OK);
    }
    // 로그아웃
    // 마이페이지
}
