package com.example.microstone.repository;

import com.example.microstone.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // security_config

    @Query("SELECT u FROM User u WHERE u.user_id = :user_id")
    User findByUser_id(@Param("user_id") String user_id);

    @Query("SELECT u FROM User u WHERE u.user_id = :user_id")
    Optional<User> findByUserId(@Param("user_id") String user_id);

    // 0803
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.user_id = :user_id")
    boolean existsByUser_id(@Param("user_id") String user_id);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.is_deactivated = true AND u.deactivation_requested_at <= :thresholdDate")
    List<User> findAllByIsDeactivatedAndDeactivationRequestedAtBefore(@Param("thresholdDate") LocalDateTime thresholdDate);

    // 닉네임 중복확인
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.nickname = :nickname")
    boolean existsByNickname(@Param("nickname") String nickname);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);
}
