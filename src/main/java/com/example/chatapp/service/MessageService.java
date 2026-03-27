package com.example.chatapp.service;

import com.example.chatapp.dto.MessageRequest;
import com.example.chatapp.dto.MessageResponse;
import com.example.chatapp.entity.*;
import com.example.chatapp.entity.enums.MessageStatus;
import com.example.chatapp.exception.ApiException;
import com.example.chatapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;

    public MessageResponse saveMessage(MessageRequest request) {
        Chat chat = chatRepository.findById(request.getChatId()).orElseThrow(() -> new ApiException("Chat not found"));
        User sender = userRepository.findById(request.getSenderId()).orElseThrow(() -> new ApiException("Sender not found"));
        Media media = request.getMediaId() == null ? null : mediaRepository.findById(request.getMediaId()).orElse(null);
        Message replyTo = request.getReplyToMessageId() == null ? null : messageRepository.findById(request.getReplyToMessageId()).orElse(null);

        Message message = Message.builder()
                .chat(chat)
                .sender(sender)
                .content(request.getContent())
                .media(media)
                .replyTo(replyTo)
                .status(MessageStatus.SENT)
                .encrypted(request.isEncrypted())
                .expiresAt(request.getExpiresAt())
                .build();

        message = messageRepository.save(message);
        return toDto(message);
    }

    public List<MessageResponse> getChatMessages(Long chatId) {
        return messageRepository.findByChatIdOrderByCreatedAtAsc(chatId).stream().map(this::toDto).toList();
    }

    public List<MessageResponse> searchMessages(String query) {
        return messageRepository.findByContentContainingIgnoreCase(query).stream().map(this::toDto).toList();
    }

    private MessageResponse toDto(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .chatId(message.getChat().getId())
                .senderId(message.getSender().getId())
                .content(message.getContent())
                .status(message.getStatus())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
