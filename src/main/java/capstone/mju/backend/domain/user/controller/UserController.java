package capstone.mju.backend.domain.user.controller;

import capstone.mju.backend.domain.user.dto.request.UpdateNameDto;
import capstone.mju.backend.domain.user.dto.request.UpdatePasswordDto;
import capstone.mju.backend.domain.auth.service.AuthService;
import capstone.mju.backend.domain.common.ResponseDto;
import capstone.mju.backend.domain.user.domain.User;
import capstone.mju.backend.domain.user.dto.request.LoginDto;
import capstone.mju.backend.domain.user.dto.response.LoginData;
import capstone.mju.backend.domain.user.service.UserService;
import capstone.mju.backend.global.auth.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@Tag(name = "User API", description = "유저 관련 API")
public class UserController {
    private final AuthService authService;
    private final UserService userService;
    // 로그인
    @PostMapping("/user/login")
    public ResponseEntity<ResponseDto<LoginData>> login(@RequestBody LoginDto loginDto, HttpServletResponse request) {
        log.info("login controller 진입");
        LoginData loginData = authService.login(loginDto, request);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "login successfully", loginData), HttpStatus.OK);
    }

    // 로그아웃
    @PostMapping("/user/logout")
    public ResponseEntity<ResponseDto<String>> logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("AccessToken", "")
                .maxAge(0)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", deleteCookie.toString());

        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "logout successfully", null), HttpStatus.OK);
    }

    // 이름 수정
    @PutMapping("/user/name")
    public ResponseEntity<ResponseDto<String>> updateName(@AuthenticatedUser User user, @RequestBody UpdateNameDto dto) {
        log.info(user.getUsername());
        String newName = userService.updateName(user, dto.getNewName());
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "Name updated successfully", newName), HttpStatus.OK);
    }

    // 비밀번호 수정
    @PutMapping("/user/ password")
    public ResponseEntity<ResponseDto<Void>> updatePassword(@AuthenticatedUser User user, @RequestBody UpdatePasswordDto dto) {
        userService.updatePassword(user, dto.getCurrentPassword(), dto.getNewPassword());
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "Password updated successfully"), HttpStatus.OK);
    }
}
