package com.example.chatapp.service;

import com.example.chatapp.entity.Media;
import com.example.chatapp.entity.Story;
import com.example.chatapp.entity.User;
import com.example.chatapp.exception.ApiException;
import com.example.chatapp.repository.MediaRepository;
import com.example.chatapp.repository.StoryRepository;
import com.example.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryService {
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;

    public Story createStory(Long userId, Long mediaId, String caption) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User not found"));
        Media media = mediaRepository.findById(mediaId).orElseThrow(() -> new ApiException("Media not found"));

        Story story = Story.builder()
                .user(user)
                .media(media)
                .caption(caption)
                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .build();
        return storyRepository.save(story);
    }

    public List<Story> activeStories() {
        return storyRepository.findByExpiresAtAfter(Instant.now());
    }
}
