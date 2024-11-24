package com.example.microstone.domain;

import com.example.microstone.domain.Id.UserBlockId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserBlock extends BaseTimeEntity {
    // 유저 개인이 개인을 차단했을때 기록되는 테이블

    @EmbeddedId
    private UserBlockId id; // 복합키

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("uid")
    @JoinColumn(name = "uid", nullable = false)
    private User uid; // 블락한 유저 UID

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("blocked_uid")
    @JoinColumn(name = "blocked_uid", nullable = false)
    private User blocked_uid; // 블락된 유저 UID


//    @Column(name = "created_at", updatable = false, nullable = false)
//    private LocalDateTime created_at; // 생성시간
}