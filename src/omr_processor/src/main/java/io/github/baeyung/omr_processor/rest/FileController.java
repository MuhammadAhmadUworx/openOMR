package io.github.baeyung.omr_processor.rest;

import io.github.baeyung.omr_processor.models.Invoice;
import io.github.baeyung.omr_processor.processors.OMRService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
public class FileController
{

    private final OMRService omrService;

    public FileController(OMRService omrService)
    {
        this.omrService = omrService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello()
    {
        return ResponseEntity.ok("hello this endpoint works");
    }

    @PostMapping(
            value = "/omr/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<byte[]> processOMRImages(@RequestParam("files") List<MultipartFile> files)
    {

        if (files.isEmpty())
        {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        byte[] zippedFile = this.omrService.processOMRImages(files);

        if (zippedFile == null)
        {
            ResponseEntity
                    .internalServerError()
                    .build();
        }

        // Prepare response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=images.zip");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zippedFile);
    }
}
