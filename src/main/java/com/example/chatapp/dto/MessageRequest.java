package com.example.chatapp.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class MessageRequest {
    private Long chatId;
    private Long senderId;
    private String content;
    private Long mediaId;
    private Long replyToMessageId;
    private boolean encrypted;
    private Instant expiresAt;
}
