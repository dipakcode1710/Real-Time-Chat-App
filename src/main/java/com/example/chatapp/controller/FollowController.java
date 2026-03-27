package com.example.chatapp.controller;

import com.example.chatapp.entity.Follow;
import com.example.chatapp.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public Follow follow(@RequestParam Long followerId, @RequestParam Long followingId) {
        return followService.follow(followerId, followingId);
    }

    @DeleteMapping
    public void unfollow(@RequestParam Long followerId, @RequestParam Long followingId) {
        followService.unfollow(followerId, followingId);
    }
}
