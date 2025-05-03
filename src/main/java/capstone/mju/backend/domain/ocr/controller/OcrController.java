package capstone.mju.backend.domain.ocr.controller;

import capstone.mju.backend.domain.ocr.dto.request.ConfirmReq;
import capstone.mju.backend.domain.ocr.dto.response.ConfirmRes;

import capstone.mju.backend.domain.ocr.dto.response.ScanRes;
import capstone.mju.backend.domain.ocr.service.ClovaOcrService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ocr")
public class OcrController {

    private final ClovaOcrService clovaOcrService;

    @PostMapping("/scan")
    public ResponseEntity<ScanRes> scan(@RequestParam("file") MultipartFile file) throws Exception {
        File tempFile = File.createTempFile("upload", file.getOriginalFilename());
        file.transferTo(tempFile);

        ScanRes res = clovaOcrService.scan(tempFile);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/confirm")
    public ResponseEntity<ConfirmRes> confirm(@RequestBody ConfirmReq req) {
        ConfirmRes res = clovaOcrService.confirm(req);
        return ResponseEntity.ok(res);
    }
}