package com.example.microstone.repository;

import com.example.microstone.domain.Enum.JoinStatus;
import com.example.microstone.domain.Id.MemberListId;
import com.example.microstone.domain.MemberList;
import com.example.microstone.domain.StudyGroup;
import com.example.microstone.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberListRepository extends JpaRepository<MemberList, MemberListId> {

    // 특정 그룹의 특정 사용자를 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM MemberList ml WHERE ml.group_id.group_id = :groupId AND ml.uid.user_id = :userId")
    void deleteByGroupIdAndUserId2(@Param("groupId") Long groupId, @Param("userId") String userId);

    // 특정 스터디 그룹에 특정 사용자가 속해있는지 확인
    @Query("SELECT COUNT(ml) > 0 FROM MemberList ml WHERE ml.group_id.group_id = :groupId AND ml.uid.user_id = :userId")
    boolean existsByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") String userId);

    // 특정 사용자와 스터디 그룹에 해당하는 MemberList 엔티티를 조회함
    @Query("SELECT ml FROM MemberList ml WHERE ml.uid = :user AND ml.group_id = :studyGroup")
    Optional<MemberList> findByUserAndGroupId(@Param("user") User user, @Param("studyGroup") StudyGroup studyGroup);

    // 특정 스터디 그룹과 유저를 기준으로 MemberList 레코드 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM MemberList ml WHERE ml.group_id.group_id = :groupId AND ml.uid.uid = :uid")
    void deleteByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("uid") Long uid);

    @Query("SELECT ml FROM MemberList ml JOIN ml.uid u WHERE u.user_id = :user_id")
    List<MemberList> findByUserId(@Param("user_id") String user_id);

    @Query("SELECT m FROM MemberList m WHERE m.group_id.group_id = :groupId AND m.join_status = :joinStatus")
    List<MemberList> findByGroup_IdAndJoinStatus(@Param("groupId") Long groupId, @Param("joinStatus") JoinStatus joinStatus);

    // 그룹 ID로 멤버를 조회하는 메소드 추가
    @Query("SELECT m FROM MemberList m WHERE m.group_id.group_id = :groupId AND m.deleted_at IS NULL")
    List<MemberList> findByGroup_id_Group_id(@Param("groupId") Long groupId);
}