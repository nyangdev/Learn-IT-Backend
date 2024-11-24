package com.example.microstone.service;

import com.example.microstone.domain.Id.UserBlockId;
import com.example.microstone.domain.User;
import com.example.microstone.domain.UserBlock;
import com.example.microstone.repository.UserBlockRepository;
import com.example.microstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// 유저차단_민지
@Service
@RequiredArgsConstructor
public class UserBlockService {

    private final UserBlockRepository userBlockRepository;
    private final UserRepository userRepository;

    // 유저 차단
    @Transactional
    public void blockUser(String blockerUsername, String blockedUsername) {

        // 차단하는 유저와 차단당하는 유저를 가져옴
        User blocker = userRepository.findByUserId(blockerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blocker user_id: " + blockerUsername));

        User blocked = userRepository.findByUserId(blockedUsername)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blocked user_id: " + blockedUsername));

        // 이미 차단된 유저인지 확인
        if(userBlockRepository.existsByUidAndBlockedUid(blocker, blocked)) {
            throw new IllegalArgumentException("User is already blocked");
        }

        UserBlock userBlock = UserBlock.builder()
                .id(new UserBlockId(blocker.getUid(), blocked.getUid()))
                .uid(blocker)
                .blocked_uid(blocked)
                .build();

        userBlockRepository.save(userBlock);
    }

    // 유저 차단 해제
    @Transactional
    public void unblockUser(String blockerUsername, String blockedUsername) {

        User blocker = userRepository.findByUserId(blockerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blocker user_id: " + blockerUsername));

        User blocked = userRepository.findByUserId(blockedUsername)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blocked user_id: " + blockedUsername));

        UserBlockId userBlockId = new UserBlockId(blocker.getUid(), blocked.getUid());
        userBlockRepository.deleteById(userBlockId);
    }

    // 차단된 유저 리스트 로드
    @Transactional(readOnly = true)
    public List<UserBlock> getBlockedUsers(String blockerUsername) {
        User blocker = userRepository.findByUserId(blockerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blocker user_id: " + blockerUsername));

        return userBlockRepository.findAllByUid(blocker);
    }

    // 차단된 유저 검색
    @Transactional(readOnly = true)
    public Optional<UserBlock> searchBlockedUser(String blockerUsername, String blockedUsername) {
        User blocker = userRepository.findByUserId(blockerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blocker user_id: " + blockerUsername));

        User blocked = userRepository.findByUserId(blockedUsername)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blocked user_id: " + blockedUsername));

        return userBlockRepository.findByUidAndBlockedUid(blocker, blocked);
    }
}