package com.example.microstone.controller;

import com.example.microstone.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
//jwt token 인증에서 /api/user 하위 경로는 체크하지않는 설정으로 인해 member 경로로 지정함
@RequiredArgsConstructor
public class MemberController {

    private final UserService userService;

    // 계정 비활성화
    @PostMapping("/deactivate")
    public ResponseEntity<Map<String, Object>> deactivateUser(@RequestParam("user_id") String user_id) {

        Map<String, Object> response = new HashMap<>();



        try {
            userService.deactivateUser(user_id);
            response.put("result", "success");
            response.put("message", "User deactivated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }



    // 비활성화 취소
//    @PostMapping("/cancel-deactivate")
//    public ResponseEntity<Map<String, Object>> cancelDeactivate(@RequestParam String user_id) {
//
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            userService.cancelDeactivate(user_id);
//            response.put("result", "success");
//            response.put("message", "User deactivation cancelled successfully");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            response.put("result", "fail");
//            response.put("message", e.getMessage());
//            return ResponseEntity.status(500).body(response);
//        }
//    }
    // issue
    // 로그인 과정에서 비활성화 취소가 자동으로 처리됨
    // 불필요한 컨트롤러

    // 30일 후 계정 완전 삭제
//    @PostMapping("/finalize-deactivate")
//    public ResponseEntity<Map<String, Object>> finalizeDeactivation() {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            userService.finalizeDeactivation();
//            response.put("result", "success");
//            response.put("message", "User finalized deactivation successfully");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            response.put("result", "fail");
//            response.put("message", e.getMessage());
//            return ResponseEntity.status(500).body(response);
//        }
//    }
    // issue
    // @Scheduled 사용으로 자동화 시킴
}