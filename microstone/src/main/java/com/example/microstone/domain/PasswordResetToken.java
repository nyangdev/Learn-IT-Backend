package com.example.microstone.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 0809 issue
// 비밀번호 재설정을 위한 테이블
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email; //사용자의 이메일 주소

    @Column(nullable = false)
    private String password_token; //랜덤하게 생성된 UUID 저장

    @Column(nullable = false)
    private LocalDateTime expiration_date; //토큰 유효기간 저장
}
