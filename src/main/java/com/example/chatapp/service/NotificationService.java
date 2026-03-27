package com.example.chatapp.service;

import com.example.chatapp.entity.Notification;
import com.example.chatapp.entity.User;
import com.example.chatapp.repository.NotificationRepository;
import com.example.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public Notification createNotification(Long userId, String type, String payload) {
        User user = userRepository.findById(userId).orElseThrow();
        Notification notification = Notification.builder().user(user).type(type).payload(payload).readFlag(false).build();
        return notificationRepository.save(notification);
    }
}
