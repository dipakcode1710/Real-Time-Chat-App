package com.example.chatapp.dto;

import com.example.chatapp.entity.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDto {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private String profilePictureUrl;
    private UserStatus status;
}
