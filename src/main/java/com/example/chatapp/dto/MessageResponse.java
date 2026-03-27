package com.example.chatapp.dto;

import com.example.chatapp.entity.enums.MessageStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class MessageResponse {
    private Long id;
    private Long chatId;
    private Long senderId;
    private String content;
    private MessageStatus status;
    private Instant createdAt;
}
