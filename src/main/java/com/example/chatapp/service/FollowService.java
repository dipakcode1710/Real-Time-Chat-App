package com.example.chatapp.service;

import com.example.chatapp.entity.Follow;
import com.example.chatapp.exception.ApiException;
import com.example.chatapp.repository.FollowRepository;
import com.example.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public Follow follow(Long followerId, Long followingId) {
        var follower = userRepository.findById(followerId).orElseThrow(() -> new ApiException("Follower not found"));
        var following = userRepository.findById(followingId).orElseThrow(() -> new ApiException("Following user not found"));

        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElse(Follow.builder().follower(follower).following(following).approved(!following.isPrivateAccount()).build());

        return followRepository.save(follow);
    }

    public void unfollow(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new ApiException("Follow relation not found"));
        followRepository.delete(follow);
    }
}
