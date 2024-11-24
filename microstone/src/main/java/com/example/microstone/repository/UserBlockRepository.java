package com.example.microstone.repository;

import com.example.microstone.domain.Id.UserBlockId;
import com.example.microstone.domain.User;
import com.example.microstone.domain.UserBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// 유저차단_민지
public interface UserBlockRepository extends JpaRepository<UserBlock, UserBlockId> {
    // 특정 유저가 특정 유저를 차단했는지 여부 확인
    @Query("SELECT COUNT(ub) > 0 FROM UserBlock ub WHERE ub.uid = :uid AND ub.blocked_uid = :blockedUid")
    boolean existsByUidAndBlockedUid(@Param("uid") User uid, @Param("blockedUid") User blockedUid);

    // 특정 유저가 차단한 모든 유저 목록 반환
    @Query("SELECT ub FROM UserBlock ub WHERE ub.uid = :uid")
    List<UserBlock> findAllByUid(@Param("uid") User uid);

    // 특정 유저가 특정 다른 유저를 차단한 기록을 반환
    @Query("SELECT ub FROM UserBlock ub WHERE ub.uid = :uid AND ub.blocked_uid = :blockedUid")
    Optional<UserBlock> findByUidAndBlockedUid(@Param("uid") User uid, @Param("blockedUid") User blockedUid);
}