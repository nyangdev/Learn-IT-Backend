package com.example.microstone.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PreprocessingPdf extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String taskId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", nullable = false)
    @JsonIgnore // issue 엔티티 무한순환참조 문제
    private User uid;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private int numberOfQuestions;
}
