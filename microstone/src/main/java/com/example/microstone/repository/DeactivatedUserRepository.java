package com.example.microstone.repository;

import com.example.microstone.domain.DeactivatedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


// 유저탈퇴_민지
public interface DeactivatedUserRepository extends JpaRepository<DeactivatedUser, Long> {
    @Query("SELECT COUNT(d) > 0 FROM DeactivatedUser d WHERE d.user_id = :user_id")
    boolean existsByUserId(@Param("user_id") String user_id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM User u JOIN DeactivatedUser d ON u.user_id = d.user_id " +
            "WHERE u.nickname = :nickname")
    boolean existsByNickname(@Param("nickname") String nickname);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM User u JOIN DeactivatedUser d ON u.user_id = d.user_id " +
            "WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);
}
