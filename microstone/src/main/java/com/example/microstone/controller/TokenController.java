package com.example.microstone.controller;

import com.example.microstone.dto.user.UserDTO;
import com.example.microstone.repository.UserRepository;
import com.example.microstone.security.JwtTokenProvider;
import com.example.microstone.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final UserRepository userRepository;

    // TODO uid가 아니라 user_id가 얻어지도록 코드 수정 필요함
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

    // Troubleshoot 1126
    private final UserService userService;

    @PostMapping("/make")
    public ResponseEntity<Map<String, String>> makeToken(@RequestBody UserDTO userDTO) {

        log.info("make token..............");

        UserDTO userDTOResult = userService.read(userDTO.getUser_id(), userDTO.getPassword());

        log.info(userDTOResult);

        return null;
    }
}