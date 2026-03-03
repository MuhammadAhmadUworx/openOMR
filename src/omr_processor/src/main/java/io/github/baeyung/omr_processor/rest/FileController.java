package io.github.baeyung.omr_processor.rest;

import io.github.baeyung.omr_processor.services.AIService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
public class FileController {

    private final AIService aiService;

    public FileController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok(this.aiService.chatWithOptions("hello how are you", "meta/llama-3.1-8b-instruct"));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> images(@RequestParam("files") List<MultipartFile> files) {
        for (MultipartFile file : files) {
            System.out.println(file.getContentType());
            System.out.println("File name: " + file.getOriginalFilename());
            System.out.println("Size: " + file.getSize());
        }

        return ResponseEntity.ok("Uploaded " + files.size() + " files");
    }
}
