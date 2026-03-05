package io.github.baeyung.omr_processor.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.baeyung.omr_processor.models.Employee;
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
    public ResponseEntity<byte[]> processOMRImages(
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("metadata") String metaData
    ) throws JsonProcessingException
    {

        if (files.isEmpty() || metaData == null)
        {
            return ResponseEntity.badRequest().build();
        }

        Employee employee = new ObjectMapper().readValue(metaData, Employee.class);

        byte[] zippedFile = this.omrService.processOMRData(files, employee);

        if (zippedFile == null)
        {
            ResponseEntity.internalServerError().build();
        }

        // Prepare response
        HttpHeaders headers = new HttpHeaders();
        var headerValue = "attachment; filename=OMR_" + employee.getForMonth() + ".zip";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zippedFile);
    }
}
