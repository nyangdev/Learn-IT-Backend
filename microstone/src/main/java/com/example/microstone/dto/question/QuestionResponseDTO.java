package com.example.microstone.dto.question;

import com.example.microstone.domain.Question;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QuestionResponseDTO {
    private Long id;
//    private String fileName;
//    private String question;
    private String correct_answer;
    private Map<String,String> explanation;
    private List<String> options;
    private String question;
    private boolean reminder;

    public QuestionResponseDTO(Question question) {
        this.id = question.getId();
//        this.fileName = question.getPdfId() != null ? question.getPdfId().getFileName() : null;
//        this.question = question.getResult() != null ? question.getResult().getQuestion() : null;
        this.correct_answer = question.getResult().getCorrect_answer();
        this.explanation = question.getResult().getExplanation();
        this.options = question.getResult().getOptions();
        this.question = question.getResult().getQuestion();
        this.reminder = question.isReminder();
    }
}
