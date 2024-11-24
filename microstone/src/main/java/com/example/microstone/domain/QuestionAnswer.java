package com.example.microstone.domain;

import com.example.microstone.domain.Enum.SelectedAnswer;
import com.example.microstone.domain.Id.QuestionAnswerId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class QuestionAnswer {
    // 질문에 대한 답변이 기록되는 테이블

    @EmbeddedId
    private QuestionAnswerId id; // 복합키

    // 여러개의 답변이 하나의 질문 post에 등록될 수 있음 (댓글과 동일 개념)
    // 해당 post에서 조회 가능해야함
    // 양방향 매핑 필요함
//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("question_post_id")
//    @JoinColumn(name = "question_post_id", nullable = false)
//    private Post question_post_id; // 질문 게시판에 등록된 게시글 ID
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("answer_id")
//    @JoinColumn(name = "answer_id", nullable = false)
//    private Post answer_id; // 답변으로 등록된 게시글 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SelectedAnswer selected_answer_check; // 답변 채택 여부
}
