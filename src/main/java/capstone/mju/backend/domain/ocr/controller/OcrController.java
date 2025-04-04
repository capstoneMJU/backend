package capstone.mju.backend.domain.ocr.controller;

import capstone.mju.backend.domain.ocr.dto.response.OcrIngredientsRes;
import capstone.mju.backend.domain.ocr.service.ClovaOcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ocr")
public class OcrController {

    private final ClovaOcrService clovaOcrService;

    @PostMapping("/ingredients")
    public ResponseEntity<OcrIngredientsRes> extractIngredients(@RequestParam("file") MultipartFile file) throws Exception {
        File tempFile = File.createTempFile("upload", file.getOriginalFilename());
        file.transferTo(tempFile);

        OcrIngredientsRes result = clovaOcrService.extractData(tempFile);

        return ResponseEntity.ok(result);
    }
}