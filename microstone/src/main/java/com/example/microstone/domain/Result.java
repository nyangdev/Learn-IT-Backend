package com.example.microstone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Primary key

    @Column(nullable = false)
    private String correct_answer;   // correct_answer

    @OneToOne
    @JoinColumn(name = "question_id") // 또는 적절한 외래 키 이름
    private Question questionId;

    @ElementCollection
    @CollectionTable(name = "explanations", joinColumns = @JoinColumn(name = "result_id"))
    @MapKeyColumn(name = "explanation_key")
    @Column(name = "explanation", nullable = false)
    private Map<String, String> explanation;

    @ElementCollection
    @CollectionTable(name = "option_value", joinColumns = @JoinColumn(name = "result_id"))
    @Column(name = "option_value", nullable = false)
    private List<String> options;

    @Column(nullable = false)
    private String question;
}
