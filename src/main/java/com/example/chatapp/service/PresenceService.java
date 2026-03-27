package com.example.chatapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PresenceService {

    private final StringRedisTemplate redisTemplate;
    private static final String ONLINE_KEY = "chat:online_users";

    public void markOnline(Long userId) {
        redisTemplate.opsForSet().add(ONLINE_KEY, String.valueOf(userId));
        redisTemplate.expire(ONLINE_KEY, Duration.ofDays(1));
    }

    public void markOffline(Long userId) {
        redisTemplate.opsForSet().remove(ONLINE_KEY, String.valueOf(userId));
    }

    public Set<String> onlineUsers() {
        Set<String> users = redisTemplate.opsForSet().members(ONLINE_KEY);
        return users == null ? Set.of() : users;
    }
}
