package com.example.microstone.repository;

import com.example.microstone.domain.PreprocessingPdf;
import com.example.microstone.domain.Question;
import com.example.microstone.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    @Query("SELECT q FROM Question q WHERE q.user = :uid AND q.pdfId = :pdfId ORDER BY q.id DESC")
    Page<Question> findAllQuestions(Pageable pageable, @Param("uid") User uid, @Param("pdfId")PreprocessingPdf pdfId);

    @Query("SELECT count(*) FROM Question q WHERE q.pdfId = :pdfId")
    long countByPdfId(@Param("pdfId") Long pdfId);

    @Query("SELECT q FROM Question q WHERE q.user = :uid AND q.pdfId = :pdfId AND q.reminder = true ORDER BY q.id DESC")
    Page<Question> findAllReminderQuestions(Pageable pageable, @Param("uid") User uid, @Param("pdfId")PreprocessingPdf pdfId);

    @Query("SELECT count(*) FROM Question q WHERE q.user = :uid AND q.pdfId = :pdfId AND q.reminder = true")
    int countReminderQuestions(@Param("uid") User uid, @Param("pdfId")PreprocessingPdf pdfId);
}
