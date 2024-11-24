package com.example.microstone.domain.Id;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class MemberListId implements Serializable {
    // 복합키를 위한 클래스

    private Long group_id;

    private Long uid;

    // 기본 생성자
    public MemberListId() {}

    // 두 개의 매개변수를 받는 생성자
    public MemberListId(Long group_id, Long uid) {
        this.group_id = group_id;
        this.uid = uid;
    }
}
