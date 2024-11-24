package com.example.microstone.service;

import com.example.microstone.domain.PasswordResetToken;
import com.example.microstone.domain.User;
import com.example.microstone.repository.PasswordResetTokenRepository;
import com.example.microstone.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

// 0809 issue
// UUID를 생성하고 이메일과 함께 MySQL에 저장하는 로직
@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private com.example.microstone.service.EmailService emailService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 랜덤한 UUID 생성
    public String createPasswordResetToken(String email) {
        String password_token = UUID.randomUUID().toString();

        // 만료 시간 설정
        // 24시간 후
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(24);

        // 토큰 정보 저장
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setEmail(email);
        passwordResetToken.setPassword_token(password_token);
        passwordResetToken.setExpiration_date(expirationTime);

        passwordResetTokenRepository.save(passwordResetToken);

        return password_token;
    }

    public void sendResetPasswordEmail(String email) {
        String password_token = createPasswordResetToken(email);
        String resetUrl = "http://localhost:3000/reset-password?password_token=" + password_token;

        // 이메일 제목
        String subject = "Learn-it 비밀번호 재설정 안내";

        String htmlBody = "<h1>Learn-it 비밀번호 재설정 안내</h1>"
                + "<p>안녕하세요,</p>"
                + "<p>비밀번호를 재설정하려면 아래 링크를 클릭하세요:</p>"
//                + "<a href=\"" + resetUrl + "\">비밀번호 재설정</a>"
                + "<a href=\"" + resetUrl + "\">" + resetUrl + "</a>"
                + "<p>이 링크는 24시간 동안 유효합니다.</p>"
                + "<p>감사합니다.</p>";

        try {
            emailService.sendHtmlMessage(email, subject, htmlBody);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // 비밀번호 재설정 처리
    public boolean resetPassword(String password_token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByPasswordToken(password_token);
        if (resetToken == null || resetToken.getExpiration_date().isBefore(LocalDateTime.now())) {
            return false;
        }

        // 유효한 토큰이므로 해당 이메일 사용자의 비밀번호 재설정
        User user = userRepository.findByEmail(resetToken.getEmail());
        if (user == null) {
            return false;
        }

        // 비밀번호 암호화 후 변경
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.changePassword(encodedPassword);
        userRepository.save(user);

        // 토큰 사용 후 삭제
        passwordResetTokenRepository.delete(resetToken);
        return true;
    }
}