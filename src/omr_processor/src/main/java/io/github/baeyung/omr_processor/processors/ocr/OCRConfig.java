package io.github.baeyung.omr_processor.processors.ocr;

import com.google.cloud.vision.v1.ImageAnnotatorClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class OCRConfig {
    @Bean
    public ImageAnnotatorClient ImageAnnotatorClient() throws IOException {
        return ImageAnnotatorClient.create();
    }
}
