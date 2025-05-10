package capstone.mju.backend.domain.user.controller;

import capstone.mju.backend.domain.user.dto.request.UpdateNameDto;
import capstone.mju.backend.domain.user.dto.request.UpdatePasswordDto;
import capstone.mju.backend.domain.auth.service.AuthService;
import capstone.mju.backend.domain.common.ResponseDto;
import capstone.mju.backend.domain.user.domain.User;
import capstone.mju.backend.domain.user.dto.request.LoginDto;
import capstone.mju.backend.domain.user.dto.response.LoginData;
import capstone.mju.backend.domain.user.dto.response.UserEmailAndNameData;
import capstone.mju.backend.domain.user.service.UserService;
import capstone.mju.backend.global.auth.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/user")
@Tag(name = "User API", description = "유저 관련 API")
public class UserController {
    private final AuthService authService;
    private final UserService userService;
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginData>> login(@RequestBody LoginDto loginDto, HttpServletResponse request) {
        log.info("login controller 진입");
        LoginData loginData = authService.login(loginDto, request);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "login successfully", loginData), HttpStatus.OK);
    }

    // 로그아웃
    @PostMapping("/logout")
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
    @PutMapping("/name")
    @Operation(summary = "이름 수정", description = "현재 로그인한 유저의 이름을 새 이름으로 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이름 수정 성공")
    })
    public ResponseEntity<ResponseDto<String>> updateName(@Parameter(hidden = true) @AuthenticatedUser User user, @RequestBody UpdateNameDto dto) {
        log.info(user.getUsername());
        String newName = userService.updateName(user, dto.getNewName());
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "Name updated successfully", newName), HttpStatus.OK);
    }

    // 비밀번호 수정
    @PutMapping("/password")
    @Operation(summary = "비밀번호 수정", description = "현재 비밀번호를 검증한 후 새 비밀번호로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 수정 성공"),
            @ApiResponse(responseCode = "4010", description = "현재 비밀번호가 올바르지 않음"),
            @ApiResponse(responseCode = "4041", description = "유저를 찾을 수 없음")
    })
    public ResponseEntity<ResponseDto<Void>> updatePassword(@Parameter(hidden = true) @AuthenticatedUser User user, @RequestBody UpdatePasswordDto dto) {
        userService.updatePassword(user, dto.getCurrentPassword(), dto.getNewPassword());
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "Password updated successfully"), HttpStatus.OK);
    }

    // user 메일, 비밀번호 반환
    @GetMapping
    @Operation(summary = "user 메일, 비밀번호 반환", description = "user 메일, 비밀번호 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 수정 성공"),
            @ApiResponse(responseCode = "4010", description = "현재 비밀번호가 올바르지 않음"),
            @ApiResponse(responseCode = "4041", description = "유저를 찾을 수 없음")
    })
    public ResponseEntity<ResponseDto<UserEmailAndNameData>> getUserEmailAndName(@Parameter(hidden = true) @AuthenticatedUser User user) {
        UserEmailAndNameData userEmailAndNameData = userService.getUserEmailAndName(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "ok", userEmailAndNameData), HttpStatus.OK);
    }
}
