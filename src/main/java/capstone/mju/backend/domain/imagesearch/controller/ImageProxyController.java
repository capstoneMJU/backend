package capstone.mju.backend.domain.imagesearch.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/proxy")
public class ImageProxyController {

    /**
     * 외부 이미지 URL을 백엔드 서버가 중계하여 프론트로 전달하는 API입니다.
     * 프론트에서 직접 외부 이미지를 호출할 경우 SSL 인증서 오류 등의 문제가 발생할 수 있어,
     * 백엔드가 바이너리 이미지를 받아 프록시로 전달하는 방식으로 이를 우회합니다.
     */
    @Operation(
            summary = "외부 이미지 URL 프록시 중계",
            description = "브라우저에서 SSL 인증 오류가 발생하는 외부 이미지 URL을 백엔드가 대신 요청하여 이미지 바이너리로 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 바이너리 응답 성공"),
            @ApiResponse(responseCode = "502", description = "외부 이미지 서버 접근 실패 또는 URL 오류")
    })
    @GetMapping
    public ResponseEntity<byte[]> proxyImage(
            @Parameter(description = "외부 이미지 URL", example = "https://i2.ruliweb.com/img/21/05/25/179a052b55383901.jpg")
            @RequestParam String url
    ) {
        try {
            // 외부 이미지 URL에 연결
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            // 서버가 User-Agent 없으면 차단할 수 있어 기본 UA 설정
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            // 응답 Content-Type 추출 (image/jpeg, image/png 등)
            String contentType = connection.getContentType();

            // 이미지 데이터를 byte[]로 읽기
            InputStream inputStream = connection.getInputStream();
            byte[] imageBytes = inputStream.readAllBytes();  // Java 9+ (Java 8은 StreamUtils 사용)

            // 응답 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType)); // 클라이언트가 인식할 수 있도록 Content-Type 지정
            headers.setCacheControl(CacheControl.noCache()); // 캐시 방지

            // 이미지 바이너리 응답 반환
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            // 외부 서버 연결 오류 등 예외 발생 시 502 Bad Gateway
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }
}
