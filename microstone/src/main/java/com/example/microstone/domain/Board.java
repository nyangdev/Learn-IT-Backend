package com.example.microstone.domain;

import com.example.microstone.domain.Enum.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long board_id; // 게시판 고유 번호

    @Column(nullable = false)
    private String board_name; // 게시판명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category; // 게시판 카테고리

    // 전체 공개 여부를 나타내는 필드
    @Column(nullable = false)
    private boolean isPublic; // true면 전체 공개, false면 스터디 그룹 내 게시판

    // 양방향 매핑
    // 하나의 board에서 여러개의 post가 보여짐
    @OneToMany(mappedBy = "board_id", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // issue 엔티티 무한순환참조 문제
    private List<Post> postList; // board에서 게시글 목록


    // null 값일때는 전체 게시판
    // group_id 존재시에는 스터디 그룹내에 있는 게시판
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = true)
    private StudyGroup group_id;

    public void changeBoardName(String board_name) {
        this.board_name = board_name;
    }
}
