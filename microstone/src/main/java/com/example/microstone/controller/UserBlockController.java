package com.example.microstone.controller;

import com.example.microstone.domain.UserBlock;
import com.example.microstone.dto.user.UserBlockDTO;
import com.example.microstone.service.UserBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/block")
@RequiredArgsConstructor
public class UserBlockController {

    // 유저차단_민지
    private final UserBlockService userBlockService;

    // 유저 차단
    @PostMapping("/{blockedUsername}")
    public ResponseEntity<Map<String, String>> blockUser(@PathVariable("blockedUsername") String blockedUsername,
                                          Principal principal) {

        Map<String, String> response = new HashMap<>();

        try {
            String blockerUsername = principal.getName();
            userBlockService.blockUser(blockerUsername, blockedUsername);
            response.put("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // 유저 차단 해제
    @DeleteMapping("/{blockedUsername}")
    public ResponseEntity<Map<String, String>> unblockUser(@PathVariable("blockedUsername") String blockedUsername,
                                                           Principal principal) {

        Map<String, String> response = new HashMap<>();

        try {
            String blockerUsername = principal.getName();
            userBlockService.unblockUser(blockerUsername, blockedUsername);
            response.put("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // 차단한 유저 리스트 로드
    // 현재 사용자가 차단한 유저들의 목록 반환
    @GetMapping
    public ResponseEntity<Map<String, Object>> getBlockedUsers(Principal principal) {
        Map<String, Object> response = new HashMap<>();

        try {
            String blockerUsername = principal.getName();
            List<UserBlockDTO> blockedUsers = userBlockService.getBlockedUsers(blockerUsername)
                    .stream()
                    .map(userBlock -> new UserBlockDTO(
                            userBlock.getUid().getUser_id(),
                            userBlock.getBlocked_uid().getUser_id(),
                            userBlock.getCreatedAt()
                    ))
                            .collect(Collectors.toList());
            response.put("result", "success");
            response.put("blockedUsers", blockedUsers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // 차단한 유저 검색
    // 사용자에게 특정 유저가 차단된 상태인지 확인함
    @GetMapping("/{blockedUsername}")
    public ResponseEntity<Map<String, Object>> searchBlockedUser(@PathVariable("blockedUsername") String blockedUsername,
                                                                 Principal principal) {

        Map<String, Object> response = new HashMap<>();

        try {
            String blockerUsername = principal.getName();
            Optional<UserBlock> userBlock = userBlockService.searchBlockedUser(blockerUsername, blockedUsername);

            if (userBlock.isPresent()) {
                response.put("result", "blocked"); // 이미 차단된 상태이면
            } else {
                response.put("result", "unblocked");
                response.put("message", "User not blocked"); // 차단되지않은 상태이면
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "error");
            response.put("message", e.getMessage()); // 그외 에러 출력
            return ResponseEntity.status(500).body(response);
        }
    }
}