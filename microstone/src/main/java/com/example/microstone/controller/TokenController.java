package com.example.microstone.controller;

import com.example.microstone.dto.user.UserDTO;
import com.example.microstone.repository.UserRepository;
import com.example.microstone.security.util.JWTUtil;
import com.example.microstone.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/api/token")
public class TokenController {

    // Troubleshoot 1126
    private final UserService userService;

    private final JWTUtil jwtUtil;

    @PostMapping("/make")
    public ResponseEntity<Map<String, String>> makeToken(@RequestBody UserDTO userDTO) {

        log.info("make token..............");

        UserDTO userDTOResult = userService.read(userDTO.getUser_id(), userDTO.getPassword());

        log.info(userDTOResult);

        String user_id = userDTOResult.getUser_id();

        Map<String, Object> dataMap = userDTOResult.getDataMap();

        String accessToken = jwtUtil.createToken(dataMap, 10);
        String refreshToken = jwtUtil.createToken(Map.of("user_id", user_id), 60 * 24 * 7);

        log.info("accessToken: " + accessToken);
        log.info("refreshToken: " + refreshToken);

        return ResponseEntity.ok(Map.of("access_token", accessToken, "refresh_token", refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader("Authorization") String accessTokenStr,
                                                            @RequestParam("refreshToken") String refreshToken,
                                                            @RequestParam("user_id") String user_id) {

        // 토큰 존재 확인
        // 검증
        log.info("access token with Bearer............" + accessTokenStr);

        if(accessTokenStr == null || !accessTokenStr.startsWith("Bearer ")) {
            return handleException("No Access Token", 400);
        }
        
        if(refreshToken == null) {
            return handleException("No Refresh Token", 400);
        }
        
        log.info("refresh token................ " + refreshToken);
        
        if(user_id == null) {
            return handleException("No User ID", 400);
        }

        // Access Token이 만료되었는지 확인
        String accessToken = accessTokenStr.substring(7);

        try {
            jwtUtil.validateToken(accessToken);

            Map<String, String> data = makeData(user_id, accessToken, refreshToken);

            log.info("Access Token is not expired..........");

            return ResponseEntity.ok(data);

        } catch (io.jsonwebtoken.ExpiredJwtException expiredJwtException) {

            // 만료되었을때 상황
            // 정상적인 상황임
            // refresh 필요

            Map<String, String> newTokenMap = makeNewToken(user_id, refreshToken);

            return ResponseEntity.ok(newTokenMap);

        } catch (Exception e) {
            return handleException("REFRESH " + e.getMessage(), 400);
        }

        // Refresh Token 검증

        // Refresh Token에서 user_id 추출

        // 새로운 Access Token, Refresh Token 생성

        // 전송

    }

    // 토큰이 만료되지 않았을때
    private Map<String, String> makeData(String user_id, String accessToken, String refreshToken) {

        return Map.of("user_id", user_id, "access_token", accessToken, "refresh_token", refreshToken);
    }

    // 기존 AccessToken이 만료되었다면 RefreshToken을 검증하고 새로운 토큰을 발행함
    private Map<String, String> makeNewToken(String user_id, String refreshToken) {

        try {
            Map<String, Object> claims = jwtUtil.validateToken(refreshToken);

            log.info("refresh token claims: " + claims);

            if(!user_id.equals(claims.get("user_id").toString())) {
                throw new RuntimeException("Invalid Refresh Token Host");
            }

            // 사용자 정보 재확인
            UserDTO userDTO = userService.getByUserId(user_id);

            Map<String, Object> newClaims = userDTO.getDataMap();

            String newAccessToken = jwtUtil.createToken(newClaims, 10);
            String newRefreshToken = jwtUtil.createToken(Map.of("user_id", user_id), 60 * 24 * 7);

            return makeData(user_id, newAccessToken, newRefreshToken);

        } catch (Exception e) {
            log.info("error......check");
//            handleException(e.getMessage(), 400);

            return Map.of("error", e.getMessage());
        }
    }

    // 에러 msg 처리
    private ResponseEntity<Map<String, String>> handleException(String message, int status) {

        return ResponseEntity.status(status).body(Map.of("message", message));
    }
}