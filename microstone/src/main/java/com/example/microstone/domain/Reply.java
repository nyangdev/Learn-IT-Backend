package com.example.microstone.domain;

import com.example.microstone.domain.Enum.GroupType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

// 댓글_민지
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reply_id;

    // 여러개의 댓글을 하나의 유저가 작성할 수 있음
    // 양방향 매핑 필요
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", nullable = false)
    private User uid; // 댓글 작성 유저

    @Column(nullable = false)
    private String content; // 댓글 내용

    // 스터디그룹_이슈_전체수정
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = true)
    private StudyGroup group_id;

    // 스터디그룹_이슈_전체수정
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupType group_type;

    // 우선 이미지 제외하고 구현하기로 함 불만있냐 모 오쪼라고
//    @Column
//    private String content_img; // 댓글에 이미지 게시시, 이미지 경로

    @Column(nullable = false)
    private int recommand_num; // 댓글 추천 수

    // 여러개의 댓글이 하나의 post에 매핑될 수 있음
    // post에서 댓글들이 조회되어야 함
    // 양방향 매핑 필요
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post post_id; // 댓글이 속한 게시글 id

    // 신고 기능 양방향 매핑
    @OneToMany(mappedBy = "reply_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;


//    @Column(name = "created_at", updatable = false, nullable = false)
//    private LocalDateTime created_at; // 생성시간
//
//    @Column(name = "updated_at")
//    private LocalDateTime updated_at;

    // soft delete
    @Column(name = "deleted_at")
    private LocalDateTime deleted_at;

    public void changeContent(String content) {
        this.content = content;
    }

//    public void changeContentImg(String content_img) {
//        this.content_img = content_img;
//    }
}