package com.example.chatapp.controller;

import com.example.chatapp.entity.Story;
import com.example.chatapp.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @PostMapping
    public Story createStory(@RequestParam Long userId, @RequestParam Long mediaId, @RequestParam(required = false) String caption) {
        return storyService.createStory(userId, mediaId, caption);
    }

    @GetMapping("/active")
    public List<Story> activeStories() {
        return storyService.activeStories();
    }
}
