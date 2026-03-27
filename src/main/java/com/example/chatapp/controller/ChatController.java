package com.example.chatapp.controller;

import com.example.chatapp.dto.MessageResponse;
import com.example.chatapp.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;

    @GetMapping("/{chatId}/messages")
    public List<MessageResponse> chatMessages(@PathVariable Long chatId) {
        return messageService.getChatMessages(chatId);
    }

    @GetMapping("/messages/search")
    public List<MessageResponse> searchMessages(@RequestParam String q) {
        return messageService.searchMessages(q);
    }
}
