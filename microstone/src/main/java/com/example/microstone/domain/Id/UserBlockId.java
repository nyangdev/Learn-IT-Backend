package com.example.microstone.domain.Id;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Embeddable
public class UserBlockId implements Serializable {
    // 복합키를 위한 클래스

    private Long uid;

    private Long blocked_uid;

    public UserBlockId(Long uid, Long blocked_uid) {
        this.uid = uid;
        this.blocked_uid = blocked_uid;
    }
}