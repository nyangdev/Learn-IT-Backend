package com.example.microstone.domain;

import com.example.microstone.domain.Enum.Category;
import com.example.microstone.domain.Enum.GroupType;
import com.example.microstone.dto.post.PostUpdateDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamicUpdate
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    // 양방향 매핑 필요
    // 여러개의 post가 하나의 board에서 보여짐
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id", nullable = false)
    @JsonIgnore // issue 엔티티 무한순환참조 문제
    private Board board_id;

    // 양방향 매핑 필요
    // 여러개의 post가 하나의 user에 의해 작성됨
    // user가 본인이 작성한 post 조회 기능 필요
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", nullable = false)
    @JsonIgnore // issue 엔티티 무한순환참조 문제
    private User uid; // 게시글 작성 유저

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content; // 게시글 내용

    // 게시판 카테고리
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    // 스터디그룹에 속하는 게시글인지 구분한다
    // 스터디그룹_이슈_전체수정
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = true)
    private StudyGroup group_id;

    // 스터디그룹_이슈_전체수정
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupType group_type;

//    @Column(nullable = false)
//    private LocalDateTime post_upload_date; // 게시글 업로드 날짜
//    created_at으로 인해 불필요하다고 판단되어 주석처리함

    @Column(nullable = false)
    private int recommend_num; // 추천 수

    @Column(nullable = false)
    private int not_recommend_num; // 비추천 수

    @Column(nullable = false)
    private int views_num; // 조회수

    @Column(nullable = false)
    private int reply_num; // 댓글수

    // 질문게시판 한정
    // 양방향 매핑
//    @OneToMany(mappedBy = "question_post_id", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<QuestionAnswer> answers;

    // 양방향 매핑
    @OneToMany(mappedBy = "post_id", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reply> replies;

    // 신고 기능 양방향 매핑
    @OneToMany(mappedBy = "post_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    //CascadeType.ALL : 부모 엔티티에서 생성, 업데이트, 삭제되면 image도 동일하게 처리
    //orphanRemoval = true : 부모 엔티티에서 image를 참조 제거하면 image엔티티에서도 DB에서 삭제
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Image> images = new ArrayList<>();


//    BaseTimeEntity 상속으로 인해 불필요 - 주석처리
//    @Column(name = "created_at", updatable = false, nullable = false)
//    private LocalDateTime created_at; // 생성시간
//
//    @Column(name = "updated_at")
//    private LocalDateTime updated_at;

    // soft delete
    @Column(name = "deleted_at")
    private LocalDate deleted_at;

    public void updateContents(PostUpdateDto postUpdateDto){
        this.title = postUpdateDto.getTitle();
        this.content = postUpdateDto.getContent();
    }

    public void deletePost(){
        LocalDate now = LocalDate.now();
        this.deleted_at = now.minusMonths(1);
    }

}
