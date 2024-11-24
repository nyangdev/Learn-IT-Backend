package com.example.microstone.domain.Id;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class QuestionListId implements Serializable {
    // 복합키를 위한 클래스

    private Long question_id; // 생성된 문제 ID

    private Long question_uid; // 문제 생성한 유저 UID
}
