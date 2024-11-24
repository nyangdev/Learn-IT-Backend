package com.example.microstone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Resources extends BaseTimeEntity {
    // 강의 자료 정보가 저장되는 테이블

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resource_id;

    // User 테이블과 조인 필요
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", nullable = false)
    private User uid; // 댓글 작성 유저

    @Column(nullable = false)
    private String file_title; // 자료 제목

    @Column(nullable = false)
    private String file_path;

    // soft delete
    @Column(name = "deleted_at")
    private LocalDateTime deleted_at;
}
