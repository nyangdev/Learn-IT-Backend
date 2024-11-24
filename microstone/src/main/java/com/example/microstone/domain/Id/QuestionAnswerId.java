package com.example.microstone.domain.Id;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class QuestionAnswerId implements Serializable {
    // 복합키를 위한 클래스

    private Long question_post_id;

    private Long answer_id;
}
