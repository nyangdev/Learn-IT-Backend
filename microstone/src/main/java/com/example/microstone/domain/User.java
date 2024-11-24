package com.example.microstone.domain;

import com.example.microstone.domain.Enum.Department;
import com.example.microstone.domain.Enum.Occupation;
import com.example.microstone.domain.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid; // 유저 고유 번호

    @Column(nullable = false, unique = true)
    private String user_id; // 유저 로그인 아이디

    @Column()
    private String password; // 비밀번호

////    @Column(unique = true)
//    private String social_id; // 소셜 로그인시 해당 소셜 계정 UID

    @Column(nullable = false)
    private String name; // 유저 실명

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String phone_num;

    @Column(nullable = false, unique = true)
    private String email;
    
    // security_config
//    private boolean social; // 소셜 로그인 여부

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // 0803_수정
    // 소셜 로그인_수정화면에서 SOCIAL 값이면 안 넘어가게
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;

    // 0803_수정
    // 소셜 로그인_수정화면에서 SOCIAL 값이면 안 넘어가게하기위함
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Occupation occupation;

    // 양방향 매핑
    @OneToMany(mappedBy = "uid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    // 양방향 매핑
//    @OneToMany(mappedBy = "maker_uid", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Question> questions;

    // 양방향 매핑
    @OneToMany(mappedBy = "uid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies;

//    @Column(name = "created_at", updatable = false, nullable = false)
//    private LocalDateTime created_at; // 생성시간
//
//    @Column(name = "updated_at")
//    private LocalDateTime updated_at;

    // soft delete
    @Column(name = "deleted_at")
    private LocalDateTime deleted_at;

    // 유저탈퇴_민지
    @Column(name = "deactivation_requested_at")
    private LocalDateTime deactivation_requested_at; // 사용자의 탈퇴 요청 시간

    @Column(name = "is_deactivated")
    private boolean is_deactivated; // 계정 비활성화 여부

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePhoneNum(String phone_num) {
        this.phone_num = phone_num;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    // 유저차단
    @OneToMany(mappedBy = "uid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBlock> blockedUsers; // 차단한 유저들
    
    // issue
    // 비밀번호 변경 기능 구현 필요
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // 유저탈퇴_민지
    // 계정 비활성화
    public void deactivateAccount() {
        this.deleted_at = LocalDateTime.now();
    }
}