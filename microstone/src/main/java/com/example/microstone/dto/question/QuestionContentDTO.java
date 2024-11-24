package com.example.microstone.dto.question;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class QuestionContentDTO {
    private String message;
    private ResultDTO result;
    private String state;
    private String taskId;  // task_id의 경우 Java naming convention에 맞춰서 camelCase로 작성

    @Data
    @NoArgsConstructor
    @AllArgsConstructor

    public class ResultDTO {
        private String correct_answer;   // correct_answer
        private Map<String, String> explanation;
        private List<String> options;
        private String question;
    }
}

