package me.projects.backend.web;

import lombok.AllArgsConstructor;
import me.projects.backend.services.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatRestController {
    private ChatService chatService;

    @GetMapping("/ask")
    public String ask(String message) {
        return chatService.ragChat(message);
    }
}
