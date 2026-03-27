package com.example.chatapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "stories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Story extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Media media;

    private String caption;
    private Instant expiresAt;

    @ManyToMany
    @JoinTable(name = "story_viewers",
            joinColumns = @JoinColumn(name = "story_id"),
            inverseJoinColumns = @JoinColumn(name = "viewer_id"))
    @Builder.Default
    private Set<User> viewers = new HashSet<>();
}
