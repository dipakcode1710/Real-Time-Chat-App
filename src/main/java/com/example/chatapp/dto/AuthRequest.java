package com.example.chatapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank
    private String usernameOrEmail;
    @NotBlank
    private String password;
}
