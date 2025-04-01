package com.example.winterdeom.domain.auth.controller;

import com.example.winterdeom.domain.auth.dto.request.LoginDto;
import com.example.winterdeom.domain.auth.service.AuthService;
import com.example.winterdeom.domain.common.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "Auth API", description = "회원가입 관련 API")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록")
    @PostMapping("/auth/join")
    public ResponseEntity<ResponseDto<Void>> join(@Valid @RequestBody LoginDto joinDto, HttpServletResponse response) {
        this.authService.join(joinDto, response);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "join successfully"), HttpStatus.OK);
    }
}