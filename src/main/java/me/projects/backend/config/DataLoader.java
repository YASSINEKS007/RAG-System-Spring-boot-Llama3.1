package me.projects.backend.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class DataLoader {
    @Value("classpath:/pdfs/pdf1.pdf")
    private Resource resource;

    @Value("store-data-v1.json")
    private String storeFile;

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = new SimpleVectorStore(embeddingModel);
        String fileStore = "C:/Users/user/OneDrive/Bureau/Projects/Rag-System-Spring-boot/backend/src/main/resources/store/" + storeFile;
        File file = new File(fileStore);

        if (!file.exists()) {
            PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(resource);
            List<Document> documents = pagePdfDocumentReader.get();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> chunks = textSplitter.split(documents);
            vectorStore.accept(chunks);
            System.out.println("works");
            vectorStore.save(file);
        } else {
            vectorStore.load(file);
        }
        return vectorStore;
    }

}
