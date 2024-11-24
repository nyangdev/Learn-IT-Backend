package com.example.microstone.repository;

import com.example.microstone.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // 토큰을 기준으로 PasswordResetToken 엔티티 조회
    @Query("SELECT p FROM PasswordResetToken p WHERE p.password_token = :password_token")
    PasswordResetToken findByPasswordToken(@Param("password_token") String password_token);
}
