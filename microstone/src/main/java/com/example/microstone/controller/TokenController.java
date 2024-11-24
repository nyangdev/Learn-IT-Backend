package com.example.microstone.controller;

import com.example.microstone.repository.UserRepository;
import com.example.microstone.security.JwtTokenProvider;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TokenController {
    private final UserRepository userRepository;

    public TokenController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/get-token/get-uid")
    public ResponseEntity<?> getUidFromToken(@RequestHeader("Authorization") String token) {
        // 'Bearer ' 접두사 제거
        String accessToken = token.replace("Bearer ", "");

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
//        String user_id = jwtTokenProvider.getUserIdFromToken(accessToken);
        Long uid = jwtTokenProvider.getUidFromToken(accessToken);

        if (uid != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("user_id", userRepository.findById(uid).get().getUser_id());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}