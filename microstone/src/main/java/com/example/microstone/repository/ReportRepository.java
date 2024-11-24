package com.example.microstone.repository;

import com.example.microstone.domain.Enum.ReportStatus;
import com.example.microstone.domain.Post;
import com.example.microstone.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {
//    @Query("SELECT r FROM Report r WHERE r.post_id IS NOT NULL ORDER BY r.report_id DESC")
//    Page<Report> findAllPostReport(@Param("reportStatus") ReportStatus reportStatus, Pageable pageable);
//
//    @Query("SELECT r FROM Report r WHERE r.reply_id IS NOT NULL ORDER BY r.report_id DESC")
//    Page<Report> findAllReplyReport(@Param("reportStatus") ReportStatus reportStatus, Pageable pageable);

    // POST 유형의 신고 목록을 상태에 따라 조회 (ReportStatus 필터링 추가)
    @Query("SELECT r FROM Report r WHERE r.post_id IS NOT NULL AND r.status = :reportStatus ORDER BY r.report_id DESC")
    Page<Report> findAllPostReport(@Param("reportStatus") ReportStatus reportStatus, Pageable pageable);

    // REPLY 유형의 신고 목록을 상태에 따라 조회 (ReportStatus 필터링 추가)
    @Query("SELECT r FROM Report r WHERE r.reply_id IS NOT NULL AND r.status = :reportStatus ORDER BY r.report_id DESC")
    Page<Report> findAllReplyReport(@Param("reportStatus") ReportStatus reportStatus, Pageable pageable);
}
