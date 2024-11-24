package com.example.microstone.domain;

import com.example.microstone.domain.Enum.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

// 스터디그룹
// 가입요청
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long group_id;

    @Column(nullable = false)
    private String user_id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status; // 요청 상태

    @Column(nullable = false)
    private String message_for_join; // 가입 신청 메세지
}