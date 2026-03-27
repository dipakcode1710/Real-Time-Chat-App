package com.example.chatapp.entity;

import com.example.chatapp.entity.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "media")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Media extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Column(nullable = false)
    private String mediaUrl;

    private Long sizeBytes;
    private boolean viewOnce;
}
