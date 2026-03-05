package io.github.baeyung.omr_processor.processors.ocr;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OCRService
{
    private final ImageAnnotatorClient vision;

    public OCRService(ImageAnnotatorClient vision)
    {
        this.vision = vision;
    }

    public String extractText(MultipartFile file)
    {
        try
        {
            ByteString imgBytes = ByteString.copyFrom(file.getBytes());

            Image img = Image
                    .newBuilder()
                    .setContent(imgBytes)
                    .build();

            Feature feat = Feature
                    .newBuilder()
                    .setType(Feature.Type.DOCUMENT_TEXT_DETECTION)
                    .build();

            AnnotateImageRequest request =
                    AnnotateImageRequest
                            .newBuilder()
                            .addFeatures(feat)
                            .setImage(img)
                            .build();

            List<AnnotateImageRequest> requests = new ArrayList<>();
            requests.add(request);

            BatchAnnotateImagesResponse response =
                    vision.batchAnnotateImages(requests);

            return response
                    .getResponses(0)
                    .getFullTextAnnotation()
                    .getText();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to process image file", e);
        }
    }
}
