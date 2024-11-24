package com.example.microstone.repository;

import com.example.microstone.domain.Enum.ManagerTransferRequestStatus;
import com.example.microstone.domain.ManagerTransferRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// 관리자이양_민지
public interface ManagerTransferRequestRepository extends JpaRepository<ManagerTransferRequest, Long> {
    @Query("SELECT mtr FROM ManagerTransferRequest mtr WHERE mtr.group.group_id = :group_id AND mtr.targetUser.uid = :uid")
    Optional<ManagerTransferRequest> findByGroupAndTargetUserId(@Param("group_id") Long group_id, @Param("uid") Long uid);

    // 특정 그룹에 진행 중인 관리자 이양 요청이 있는지 확인
    @Query("SELECT mtr FROM ManagerTransferRequest mtr WHERE mtr.group.group_id = :group_id AND mtr.status = :status")
    Optional<ManagerTransferRequest> findByGroupAndStatus(@Param("group_id") Long group_id, @Param("status") ManagerTransferRequestStatus status);
}
