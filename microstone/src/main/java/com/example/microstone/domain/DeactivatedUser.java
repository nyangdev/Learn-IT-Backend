package com.example.microstone.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 유저탈퇴_민지
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeactivatedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String user_id;

    @Column(nullable = false)
    private LocalDateTime deactivateAt; // 탈퇴처리된 날짜
}