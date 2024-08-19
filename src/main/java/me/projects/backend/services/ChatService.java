package me.projects.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Vector;

@Service
public class ChatService {

    private ChatClient client;

    private VectorStore vectorStore;

    @Value("classpath:/prompts/prompt-template.st")

    private Resource resource;

    public ChatService(ChatClient.Builder client, VectorStore vectorStore) {
        this.client = client.build();
        this.vectorStore = vectorStore;
    }

    public String ragChat(String question) {
        List<Document> documents = vectorStore.similaritySearch(question);
        List<String> context = documents.stream().map(Document::getContent).toList();
        PromptTemplate promptTemplate = new PromptTemplate(resource);
        Prompt prompt = promptTemplate.create(Map.of("context", context, "question", question));
        return client.prompt(prompt).call().content();
    }
}
