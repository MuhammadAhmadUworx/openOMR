package io.github.baeyung.omr_processor.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Value;
import io.github.baeyung.omr_processor.models.Invoice;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.stereotype.Service;

@Service
public class AIService {
    private final OpenAiChatModel chatModel;

    @Value(value = "${spring.ai.openai.chat.options.model}")
    private String globalConfiguredChatModel;

    private static String OMR_JSON_SCHEMA = """
            {
              "type": "object",
              "properties": {
                "company_name": { "type": ["string", "null"] },
                "address": { "type": ["string", "null"] },
                "phones": {
                  "type": "array",
                  "items": { "type": "string" }
                },
                "invoice_number": { "type": ["string", "null"] },
                "invoice_date": { "type": ["string", "null"] },
                "customer_code": { "type": ["string", "null"] },
                "customer_name": { "type": ["string", "null"] },
                "customer_city": { "type": ["string", "null"] },
                "items": {
                  "type": "array",
                  "items": {
                    "type": "object",
                    "properties": {
                      "product_name": { "type": ["string", "null"] },
                      "quantity": { "type": ["integer", "null"] },
                      "packing": { "type": ["string", "null"] },
                      "rate": { "type": ["number", "null"] },
                      "gross_amount": { "type": ["number", "null"] },
                      "discount_percent": { "type": ["number", "null"] },
                      "discount_rs": { "type": ["number", "null"] },
                      "sales_tax": { "type": ["number", "null"] },
                      "net_amount": { "type": ["number", "null"] }
                    },
                    "required": [
                      "product_name",
                      "quantity",
                      "packing",
                      "rate",
                      "gross_amount",
                      "discount_percent",
                      "discount_rs",
                      "sales_tax",
                      "net_amount"
                    ],
                    "additionalProperties": false
                  }
                },
                "total_items": { "type": ["integer", "null"] },
                "total_gross": { "type": ["number", "null"] },
                "total_discount": { "type": ["number", "null"] },
                "total_tax": { "type": ["number", "null"] },
                "total_net": { "type": ["number", "null"] }
              },
              "required": [
                "company_name",
                "address",
                "phones",
                "invoice_number",
                "invoice_date",
                "customer_code",
                "customer_name",
                "customer_city",
                "items",
                "total_items",
                "total_gross",
                "total_discount",
                "total_tax",
                "total_net"
              ],
              "additionalProperties": false
            }
            """;

    public AIService(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String chat(String message) {
        return chatModel.call(message);
    }

    public String chatWithOptions(String message, String model) {
        ChatOptions options = OpenAiChatOptions.builder()
                .model(model)  // Override model dynamically
                .temperature(0.8)
                .maxTokens(2000)
                .build();

        return chatModel.call(new Prompt(message, options))
                .getResult()
                .getOutput()
                .getText();
    }

    public Invoice fromOCRCreateInvoice(String ocrText) {
        ChatOptions options = OpenAiChatOptions
                .builder()
                .model(globalConfiguredChatModel)
                .responseFormat(
                        new ResponseFormat(
                                ResponseFormat.Type.JSON_SCHEMA,
                                OMR_JSON_SCHEMA
                        )
                )
                .temperature(0.0)
                .build();

        String llmResponse = chatModel.call(
                new Prompt(
                        """
                                Extract structured invoice data from the following OCR text.
                                Return only valid JSON matching the schema.
                                
                                If a value is not present in the OCR text, return null.
                                Do not calculate missing totals.
                                Do not infer values not explicitly written.
                                
                                OCR TEXT:
                                
                                """ + ocrText,
                        options
                )
        )
                .getResult()
                .getOutput()
                .getText();

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(llmResponse, Invoice.class);
        } catch (Exception e)
        {
            System.out.println(llmResponse);
            throw new RuntimeException(e);
        }
    }
}
