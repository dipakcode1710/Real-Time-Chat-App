package com.example.chatapp.dto;

import lombok.Data;

@Data
public class TypingEvent {
    private Long chatId;
    private Long userId;
    private boolean typing;
}
