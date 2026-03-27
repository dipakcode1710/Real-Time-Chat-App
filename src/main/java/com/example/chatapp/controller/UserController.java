package com.example.chatapp.controller;

import com.example.chatapp.dto.UserProfileDto;
import com.example.chatapp.service.PresenceService;
import com.example.chatapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PresenceService presenceService;

    @GetMapping("/{id}")
    public UserProfileDto profile(@PathVariable Long id) {
        return userService.getProfile(id);
    }

    @GetMapping("/search")
    public List<UserProfileDto> search(@RequestParam String q) {
        return userService.searchUsers(q);
    }

    @GetMapping("/online")
    public Set<String> onlineUsers() {
        return presenceService.onlineUsers();
    }
}
