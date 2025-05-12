package capstone.mju.backend.domain.auth.controller;

import capstone.mju.backend.domain.auth.dto.request.JoinDto;
import capstone.mju.backend.domain.auth.service.AuthService;
import capstone.mju.backend.domain.common.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "Auth API", description = "회원가입 관련 API")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록")
    @PostMapping("/api/v1/auth/join")
    public ResponseEntity<ResponseDto<Void>> join(@Valid @RequestBody JoinDto joinDto, HttpServletResponse response) {
        this.authService.join(joinDto, response);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "join successfully"), HttpStatus.CREATED);
    }
}