package com.example.chatapp.service;

import com.example.chatapp.dto.*;
import com.example.chatapp.entity.Role;
import com.example.chatapp.entity.User;
import com.example.chatapp.entity.enums.UserStatus;
import com.example.chatapp.exception.ApiException;
import com.example.chatapp.repository.RoleRepository;
import com.example.chatapp.repository.UserRepository;
import com.example.chatapp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ApiException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("Email already exists");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(UserStatus.OFFLINE)
                .lastSeen(Instant.now())
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);
        UserDetails details = userDetailsService.loadUserByUsername(user.getUsername());
        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(details))
                .refreshToken(jwtService.generateRefreshToken(details))
                .tokenType("Bearer")
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword()));
        UserDetails details = userDetailsService.loadUserByUsername(request.getUsernameOrEmail());
        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(details))
                .refreshToken(jwtService.generateRefreshToken(details))
                .tokenType("Bearer")
                .build();
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String username = jwtService.extractUsername(request.getRefreshToken());
        UserDetails details = userDetailsService.loadUserByUsername(username);
        if (!jwtService.isTokenValid(request.getRefreshToken(), details)) {
            throw new ApiException("Invalid refresh token");
        }
        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(details))
                .refreshToken(request.getRefreshToken())
                .tokenType("Bearer")
                .build();
    }
}
