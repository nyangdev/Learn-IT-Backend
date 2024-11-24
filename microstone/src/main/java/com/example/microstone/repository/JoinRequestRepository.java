package com.example.microstone.repository;

import com.example.microstone.domain.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    // 1. 전체 요청 로드
    @Query("SELECT jr FROM JoinRequest jr WHERE jr.group_id = :group_id")
    List<JoinRequest> findAllByGroupId(@Param("group_id") Long group_id);

    // 2. 가입 완료된 리스트만 로드 (status가 APPROVED인 경우)
    @Query("SELECT jr FROM JoinRequest jr WHERE jr.group_id = :group_id AND jr.status = com.example.microstone.domain.Enum.RequestStatus.APPROVED")
    List<JoinRequest> findApprovedByGroupId(@Param("group_id") Long group_id);

    // 3. 가입 미완료된 리스트만 로드 (status가 PENDING 또는 REJECTED인 경우)
    @Query("SELECT jr FROM JoinRequest jr WHERE jr.group_id = :group_id AND jr.status IN (com.example.microstone.domain.Enum.RequestStatus.PENDING, com.example.microstone.domain.Enum.RequestStatus.REJECTED)")
    List<JoinRequest> findNotApprovedByGroupId(@Param("group_id") Long group_id);

    // 가입 요청 롤백해야하는 경우
    // 1. 신청상태가 PENDING 일때 (대기중)
    // 2. APPROVED 일때 (승인)
    // REJECTED 일때는 다시 가입 요청을 보낼 수 있게함

    // 신청 상태가 PENDING, APPROVED 인 경우
    @Query("SELECT jr FROM JoinRequest jr WHERE jr.group_id = :group_id AND jr.user_id = :user_id AND (jr.status = 'PENDING' OR jr.status = 'APPROVED')")
    Optional<JoinRequest> findPendingOrApprovedRequest(@Param("group_id") Long group_id, @Param("user_id") String user_id);

    @Modifying
    @Transactional
    @Query("DELETE FROM JoinRequest jr WHERE jr.user_id = :user_id AND jr.group_id = :group_id")
    void deleteByUserIdAndGroupId(@Param("user_id") String user_id, @Param("group_id") Long group_id);
}