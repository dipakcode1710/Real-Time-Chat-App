package com.example.chatapp.websocket;

import com.example.chatapp.dto.MessageRequest;
import com.example.chatapp.dto.MessageResponse;
import com.example.chatapp.dto.TypingEvent;
import com.example.chatapp.service.MessageService;
import com.example.chatapp.service.NotificationService;
import com.example.chatapp.service.PresenceService;
import com.example.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.AccessDeniedException;

import java.security.Principal;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final MessageService messageService;
    private final PresenceService presenceService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload MessageRequest request, Principal principal) {
        Long authenticatedUserId = resolveAuthenticatedUserId(principal);
        request.setSenderId(authenticatedUserId);
        MessageResponse saved = messageService.saveMessage(request);
        messagingTemplate.convertAndSend("/topic/messages/" + request.getChatId(), saved);
        messagingTemplate.convertAndSendToUser(String.valueOf(authenticatedUserId), "/queue/private", saved);
        notificationService.createNotification(authenticatedUserId, "NEW_MESSAGE", "Message sent in chat " + request.getChatId());
    }

    @MessageMapping("/chat.typing")
    public void typing(@Payload TypingEvent event, Principal principal) {
        resolveAuthenticatedUserId(principal);
        messagingTemplate.convertAndSend("/topic/typing/" + event.getChatId(), event);
    }

    @MessageMapping("/chat.presence.online")
    public void online(@Payload Long userId, Principal principal) {
        Long authenticatedUserId = resolveAuthenticatedUserId(principal);
        if (!authenticatedUserId.equals(userId)) {
            throw new AccessDeniedException("Cannot mark presence for another user");
        }
        presenceService.markOnline(userId);
        messagingTemplate.convertAndSend("/topic/presence", presenceService.onlineUsers());
    }

    @MessageMapping("/chat.presence.offline")
    public void offline(@Payload Long userId, Principal principal) {
        Long authenticatedUserId = resolveAuthenticatedUserId(principal);
        if (!authenticatedUserId.equals(userId)) {
            throw new AccessDeniedException("Cannot mark presence for another user");
        }
        presenceService.markOffline(userId);
        messagingTemplate.convertAndSend("/topic/presence", presenceService.onlineUsers());
    }

    private Long resolveAuthenticatedUserId(Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Unauthenticated WebSocket request");
        }
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new AccessDeniedException("Authenticated user not found"))
                .getId();
    }
}
