package com.example.microstone.controller;

import com.example.microstone.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    // 비밀번호 재설정 이메일 전송 요청 처리
    @PostMapping("/send")
    public ResponseEntity<String> sendResetPasswordEmail(@RequestParam("email") String email) {
        passwordResetService.sendResetPasswordEmail(email);
        return ResponseEntity.ok("비밀번호 재설정 이메일이 발송되었습니다.");
    }

    // 실제 비밀번호 재설정
    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestParam("password_token") String password_token, @RequestParam("newPassword") String newPassword) {
        boolean isResetSuccessful = passwordResetService.resetPassword(password_token, newPassword);

        Map<String, String> response = new HashMap<>();
        if (isResetSuccessful) {
            response.put("message", "비밀번호가 성공적으로 재설정되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "비밀번호 재설정에 실패했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
}