package com.example.microstone.repository;

import com.example.microstone.domain.Id.QuestionListId;
import com.example.microstone.domain.QuestionList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionListRepository extends JpaRepository<QuestionList, QuestionListId> {
}
