package com.example.microstone.repository;

import com.example.microstone.domain.PreprocessingPdf;
import com.example.microstone.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PreprocessingPdfRepository extends JpaRepository<PreprocessingPdf, Long> {
    @Query("SELECT p FROM PreprocessingPdf p WHERE p.uid = :uid ORDER BY p.id DESC")
    Page<PreprocessingPdf> findAllpdfs(Pageable pageable, @Param("uid") User uid);

    @Query("SELECT p FROM PreprocessingPdf p WHERE p.taskId = :taskId")
    PreprocessingPdf findByTaskId(@Param("taskId") String taskId);


}
