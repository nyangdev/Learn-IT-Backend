package com.example.microstone.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Primary key

    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", nullable = false)
    @JsonIgnore // issue 엔티티 무한순환참조 문제
    private User user; // 게시글 작성 유저

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pdf_id", nullable = false)
    @JsonIgnore // issue 엔티티 무한순환참조 문제
    private PreprocessingPdf pdfId; // 게시글 작성 유저

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Result result;

    private String state;

    private String taskId;  // task_id를 camelCase로 변경

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private boolean reminder;



}