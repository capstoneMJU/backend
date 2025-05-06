package capstone.mju.backend.domain.ocr.controller;

import capstone.mju.backend.domain.ocr.dto.request.ConfirmReq;
import capstone.mju.backend.domain.ocr.dto.response.ConfirmRes;

import capstone.mju.backend.domain.ocr.dto.response.ScanRes;
import capstone.mju.backend.domain.ocr.service.ClovaOcrService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ocr")
@Tag(name = "OCR API", description = "CLOVA OCR을 활용한 식품 정보 스캔 및 대체당 정보 추출 API")
public class OcrController {

    private final ClovaOcrService clovaOcrService;

    @Operation(
            summary = "OCR 스캔",
            description = "이미지 파일을 업로드하여 OCR을 수행하고, 텍스트 및 품목번호와 제품명을 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OCR 스캔 성공",
                    content = @Content(schema = @Schema(implementation = ScanRes.class))),
            @ApiResponse(responseCode = "500", description = "품목번호 인식 실패 또는 해당 제품이 DB에 존재하지 않음")
    })
    @PostMapping("/scan")
    public ResponseEntity<ScanRes> scan(
            @Parameter(description = "스캔할 이미지 파일", required = true)
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        File tempFile = File.createTempFile("upload", file.getOriginalFilename());
        file.transferTo(tempFile);

        ScanRes res = clovaOcrService.scan(tempFile);

        return ResponseEntity.ok(res);
    }

    @Operation(
            summary = "감미료 정보 확인",
            description = "OCR로 인식된 텍스트에서 대체당을 찾아 관련 정보를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대체당 정보 반환 성공",
                    content = @Content(schema = @Schema(implementation = ConfirmRes.class))),
            @ApiResponse(responseCode = "500", description = "입력값 오류 또는 품목번호 불일치")
    })
    @PostMapping("/confirm")
    public ResponseEntity<ConfirmRes> confirm(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "OCR 텍스트와 품목번호",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ConfirmReq.class))
            )
            @RequestBody ConfirmReq req
    ) {
        ConfirmRes res = clovaOcrService.confirm(req);
        return ResponseEntity.ok(res);
    }
}