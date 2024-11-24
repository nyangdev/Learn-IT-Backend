package com.example.microstone.controller;

import com.example.microstone.domain.Enum.Department;
import com.example.microstone.domain.Enum.Occupation;
import com.example.microstone.domain.Enum.Role;
import com.example.microstone.domain.User;
import com.example.microstone.dto.TokenRequestDTO;
import com.example.microstone.dto.user.UserAdditionalInfoDTO;
import com.example.microstone.dto.user.UserResponse;
import com.example.microstone.dto.user.UserUpdateDTO;
import com.example.microstone.repository.UserRepository;
import com.example.microstone.service.TokenService;
import com.example.microstone.service.user.UserService;
import com.example.microstone.dto.user.UserFormDTO;
import com.example.microstone.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody UserFormDTO userFormDTO) {

        Map<String, String> response = new HashMap<>();

        log.info("------------------------------------------------------------");
        log.info(userFormDTO.toString());
        log.info("------------------------------------------------------------");

        try {
            Long userId = userService.registerUser(userFormDTO);
            response.put("result", "success");
            response.put("user_id", userId.toString());
        } catch (Exception e) {
            response.put("result", "fail");
            response.put("reason", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }

        return ResponseEntity.ok(response);
    }

    // 아이디 중복 확인 API
    @PostMapping("/check-id")
    public ResponseEntity<Map<String, Boolean>> checkUserId(@RequestBody Map<String, String> request) {
        String userId = request.get("user_id");
        boolean exists = userService.checkUserId(userId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }

    // 닉네임 중복 확인 API
    @PostMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkUserNickname(@RequestBody Map<String, String > request) {
        String userNickname = request.get("nickname");
        boolean exists = userService.checkUserNickname(userNickname);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }

    // 이메일 중복 확인 API
    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkUserEmail(@RequestBody Map<String, String> request) {
        String userEmail = request.get("email");
        boolean exists = userService.checkUserEmail(userEmail);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }

    // 소셜 로그인 리디렉션
    @PostMapping("/additional-info")
    public ResponseEntity<String> saveAdditionalInfo(@RequestParam("user_id") String user_id, @RequestBody UserAdditionalInfoDTO userAdditionalInfoDTO) {
        try {
            userService.saveAdditionalInfo(user_id, userAdditionalInfoDTO);
            return ResponseEntity.ok("추가 정보 저장 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("추가 정보 저장 실패: " + e.getMessage());
        }
    }

    @PostMapping("/check-additional-info")
    public ResponseEntity<Boolean> checkAdditionalInfo(@RequestParam("user_id") String user_id) {
        User user = userService.findByUserId(user_id);

        boolean needsAdditionalInfo = user.getDepartment() == Department.SOCIAL && user.getOccupation() == Occupation.SOCIAL;

        return ResponseEntity.ok(needsAdditionalInfo);
    }

    // Custom Response 반환
    @GetMapping("/info/{user_id}")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable("user_id") String user_id) {

        UserResponse userResponse = userService.getUserWithCustom(user_id);

        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateUserInfo(@RequestBody UserUpdateDTO userUpdateDTO) {
        Map<String, String> response = new HashMap<>();

        log.info("Received user update request: " + userUpdateDTO);

        try {
            userService.updateUser(userUpdateDTO);
            response.put("result", "success");
            response.put("message", "사용자 정보가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            log.error("Error updating user info: ", e);
            response.put("result", "fail");
            response.put("message", "사용자 정보 업데이트에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody TokenRequestDTO tokenRequest) {
        String refreshToken = tokenRequest.getRefreshToken();

        // 리프레시 토큰의 유효성 검사
        try {
            Map<String, Object> claims = jwtUtil.validateToken(refreshToken); // 유효성 검사 메서드
            Long uid = Long.parseLong(claims.get("uid").toString());

            // 사용자 조회
            Optional<User> userOptional = userRepository.findById(uid);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // 새로운 액세스 토큰 생성
                Map<String, Object> newClaims = new HashMap<>();
                newClaims.put("uid", user.getUid());
                newClaims.put("user_id", user.getUser_id());
                newClaims.put("role", user.getRole());
                newClaims.put("email", user.getEmail());

                String newAccessToken = jwtUtil.generateToken(newClaims, 10); // 10분 유효

                // 응답으로 새로운 액세스 토큰 반환
                Map<String, String> response = new HashMap<>();
                response.put("access_token", newAccessToken);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "사용자를 찾을 수 없습니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "리프레시 토큰이 유효하지 않습니다."));
        }
    }

    @GetMapping("/verifyingAdmin")
    public ResponseEntity<?> verifyingAdmin(@RequestHeader("Authorization")String token) {
       Long uid = tokenService.getUidFromToken(token);
       Role role = userRepository.findById(uid).get().getRole();
       Map<String, Object> map = new HashMap<>();
       map.put("role", role);
       return ResponseEntity.ok(map);
    }

}
