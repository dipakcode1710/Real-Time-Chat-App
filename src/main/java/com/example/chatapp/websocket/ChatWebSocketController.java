package com.example.chatapp.websocket;

import com.example.chatapp.dto.MessageRequest;
import com.example.chatapp.dto.MessageResponse;
import com.example.chatapp.dto.TypingEvent;
import com.example.chatapp.service.MessageService;
import com.example.chatapp.service.NotificationService;
import com.example.chatapp.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final MessageService messageService;
    private final PresenceService presenceService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload MessageRequest request) {
        MessageResponse saved = messageService.saveMessage(request);
        messagingTemplate.convertAndSend("/topic/messages/" + request.getChatId(), saved);
        messagingTemplate.convertAndSendToUser(String.valueOf(request.getSenderId()), "/queue/private", saved);
        notificationService.createNotification(request.getSenderId(), "NEW_MESSAGE", "Message sent in chat " + request.getChatId());
    }

    @MessageMapping("/chat.typing")
    public void typing(@Payload TypingEvent event) {
        messagingTemplate.convertAndSend("/topic/typing/" + event.getChatId(), event);
    }

    @MessageMapping("/chat.presence.online")
    public void online(@Payload Long userId) {
        presenceService.markOnline(userId);
        messagingTemplate.convertAndSend("/topic/presence", presenceService.onlineUsers());
    }

    @MessageMapping("/chat.presence.offline")
    public void offline(@Payload Long userId) {
        presenceService.markOffline(userId);
        messagingTemplate.convertAndSend("/topic/presence", presenceService.onlineUsers());
    }
}
