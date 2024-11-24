package com.example.microstone.repository;

import com.example.microstone.domain.Post;
import com.example.microstone.domain.Reply;
import com.example.microstone.domain.StudyGroup;
import com.example.microstone.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // 댓글_민지

    // hard delete 용 메서드
    // 일정 기간이 지난 후 삭제된 댓글 조회
    @Query("SELECT r FROM Reply r WHERE r.deleted_at < :Date")
    List<Reply> findAllByDeleted_atBefore(@Param("Date") LocalDateTime Date);

    // 탈퇴_민지
    // 탈퇴_민지
    @Query("SELECT r FROM Reply r WHERE r.uid = :user AND r.group_id = :studyGroup")
    List<Reply> findByUserAndGroupId(@Param("user") User user, @Param("studyGroup") StudyGroup studyGroup);

    @Query("SELECT r FROM Reply r WHERE r.deleted_at IS NULL AND r.post_id = :post_id ORDER BY r.reply_id DESC")
    Page<Reply> findAllReply(Pageable pageable, @Param("post_id")Post postId);
}