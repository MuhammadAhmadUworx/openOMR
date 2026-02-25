package io.github.baeyung.omr_processor.rest;

import io.github.baeyung.omr_processor.services.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    private final AIService aiService;

    public ImageController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok(this.aiService.chatWithOptions("hello how are you", "meta/llama-3.1-8b-instruct"));
    }
}
