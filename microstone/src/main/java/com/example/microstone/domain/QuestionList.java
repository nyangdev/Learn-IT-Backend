package com.example.microstone.domain;

import com.example.microstone.domain.Enum.RemindStatus;
import com.example.microstone.domain.Id.QuestionListId;
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
public class QuestionList {
    @EmbeddedId
    private QuestionListId id; // 복합키

//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("question_id")
//    @JoinColumn(name = "question_id", nullable = false)
//    private Question question_id; // 생성한 문제 ID
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @MapsId("question_uid")
//    @JoinColumn(name = "question_uid", nullable = false)
//    private User question_uid; // 문제 생성한 유저 UID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RemindStatus remind_status;
}
