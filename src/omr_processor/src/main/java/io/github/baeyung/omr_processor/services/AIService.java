package io.github.baeyung.omr_processor.services;

import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class AIService {
    private final OpenAiChatModel chatModel;

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
}
