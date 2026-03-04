package io.github.baeyung.omr_processor.rest;

import io.github.baeyung.omr_processor.models.Invoice;
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
    public ResponseEntity<Invoice> images(@RequestParam("files") List<MultipartFile> files) {

        if (files.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                this.aiService.fromOCRCreateInvoice(
                        """
                                PH: 2611058, 2627439, 2636786, 2602118
                                INV NO:
                                INV DATE: Monday, Mar 2 2026 02:00 PM
                                16491
                                Page No.
                                1/1
                                Ayyan Medicine Company
                                Medicine Market Chiniot Bazar Faisalabad
                                CUSTOMER: 656
                                AHMAD SB,............HM
                                FAISALABAD
                                QTY PIEC
                                PRODUCT DESCRIPTION
                                2
                                0
                                VALAM 10/160MG TAB
                                PACKING BATCH RATE
                                1x14
                                707.39
                                G.Amt
                                1414.78
                                % DIS
                                0.00
                                DIS.RS STAX
                                0.00 0.00
                                N.Amt
                                1414.78
                                G.Amount
                                Total Items/Page:
                                1
                                1,414.78
                                D.Rs S.Tax
                                0.00 0.00
                                N.Amount
                                1,414.78
                                Total Items:
                                1
                                G.Amount
                                1,414.78
                                D.RS S.Tax
                                N.Amount
                                0.00 0.00
                                1,415
                                AYYAN
                                AYYAN MICOMPAN
                                PAID
                                کے دور رس نکل آئٹم کی ہیں یا تبدیلی نہیں ہوگی
                                """
                )
        );
    }
}
