package com.example.microstone.repository;

import com.example.microstone.domain.Id.QuestionAnswerId;
import com.example.microstone.domain.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, QuestionAnswerId> {
}
