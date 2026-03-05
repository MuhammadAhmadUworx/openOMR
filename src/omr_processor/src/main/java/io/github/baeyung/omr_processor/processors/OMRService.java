package io.github.baeyung.omr_processor.processors;

import io.github.baeyung.omr_processor.models.File;
import io.github.baeyung.omr_processor.models.Invoice;
import io.github.baeyung.omr_processor.processors.ocr.OCRService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class OMRService
{
    private final OCRService ocrService;
    private final AIService aiService;

    public OMRService(OCRService ocrService, AIService aiService)
    {
        this.ocrService = ocrService;
        this.aiService = aiService;
    }

    public byte[] processOMRImages(List<MultipartFile> images)
    {
        List<File> files = new ArrayList<>();
        int counter = 1;
        String finalText = "";

        for (MultipartFile image : images)
        {
            try
            {
                String ocrText = ocrService.extractText(image);

                if (ocrText == null || ocrText.isBlank())
                {
                    continue;
                }

                Invoice invoice = aiService.fromOCRCreateInvoice(ocrText);

                String fileName = FileProcessor.getNewFileName(
                        counter,
                        "FEB"
                );

                finalText = finalText
                        .concat("\n")
                        .concat(fileName.split("\\.")[0])
                        .concat("\t")
                        .concat(invoice.getInvoiceDate())
                        .concat("\t")
                        .concat(invoice
                                        .getTotalNet()
                                        .toString())
                        .concat("\t")
                        .concat("0")
                        .concat("\t")
                        .concat("0")
                        .concat("\t")
                        .concat("\"\"")
                        .concat("\t")
                        .concat("\"\"");
                // add concat for ai reason

                files.add(
                        new File(
                                fileName + FileProcessor.getFileExt(image.getContentType()),
                                image.getBytes()
                        )
                );

                counter++;
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        files.add(
                new File(
                        "excel_text.txt",
                        finalText.getBytes(StandardCharsets.UTF_8)
                )
        );

        try
        {
            return FileProcessor.zipAllFiles(files);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
