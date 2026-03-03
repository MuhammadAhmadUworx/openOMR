package io.github.baeyung.omr_processor.rest;

import io.github.baeyung.omr_processor.processors.AIService;
import io.github.baeyung.omr_processor.processors.ocr.OCRService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
public class FileController {

    private final AIService aiService;
    private final OCRService ocrService;

    public FileController(AIService aiService,  OCRService ocrService) {
        this.aiService = aiService;
        this.ocrService = ocrService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok(this.aiService.chatWithOptions("hello how are you", "meta/llama-3.1-8b-instruct"));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> images(@RequestParam("files") List<MultipartFile> files) {

        if (files.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(this.ocrService.extractText(files.getFirst()));
    }
}
