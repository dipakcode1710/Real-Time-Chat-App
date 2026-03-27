package com.example.chatapp.service;

import com.example.chatapp.dto.UserProfileDto;
import com.example.chatapp.entity.User;
import com.example.chatapp.exception.ApiException;
import com.example.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserProfileDto getProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User not found"));
        return toDto(user);
    }

    public List<UserProfileDto> searchUsers(String q) {
        return userRepository.findByUsernameContainingIgnoreCase(q).stream().map(this::toDto).toList();
    }

    private UserProfileDto toDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .profilePictureUrl(user.getProfilePictureUrl())
                .status(user.getStatus())
                .build();
    }
}
