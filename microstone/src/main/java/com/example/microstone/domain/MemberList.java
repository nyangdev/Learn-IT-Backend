package com.example.microstone.domain;

import com.example.microstone.domain.Enum.GroupRole;
import com.example.microstone.domain.Enum.JoinStatus;
import com.example.microstone.domain.Id.MemberListId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberList {

    @EmbeddedId
    private MemberListId id; // 복합키

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("group_id")
    @JoinColumn(name = "group_id", nullable = false)
    private StudyGroup group_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("uid")
    @JoinColumn(name = "uid", nullable = false)
    private User uid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupRole role; // 스터디 그룹 내 역할

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JoinStatus join_status; // 현재 가입 상태

    @Column(name = "deleted_at")
    private LocalDateTime deleted_at; // 삭제 날짜 필드 추가

    // 삭제 날짜 설정 메소드
    public void setDeletedAt(LocalDateTime deleted_at) {
        this.deleted_at = deleted_at;
    }

    public LocalDateTime getDeletedAt() {
        return deleted_at;
    }
    
//    @Column(nullable = false)
//    private String message_for_join; // 가입신청시 제출하는 가입 신청 메세지

}