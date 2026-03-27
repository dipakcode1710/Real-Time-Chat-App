package com.example.chatapp.entity;

import com.example.chatapp.entity.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "messages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Message extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Message replyTo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Media media;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private boolean encrypted;
    private boolean deletedForEveryone;
    private Instant expiresAt;
}
