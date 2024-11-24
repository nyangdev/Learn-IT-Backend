package com.example.microstone.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudyGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long group_id;

    @Column(nullable = false)
    private String group_name; // 스터디 그룹명

    @Column(nullable = false)
    @Max(value = 100)
    @Min(value = 1)
    private int present_member_num; // 현재 스터디 그룹 멤버 인원

    @Column(nullable = false)
    // 사용자가 선택 가능 (100명이 최대로)
    @Max(value = 100)
    private int max_member_num; // 그룹 최대 인원


//    @Column(name = "created_at", updatable = false, nullable = false)
//    private LocalDateTime created_at; // 생성시간
//
//    @Column(name = "updated_at")
//    private LocalDateTime updated_at;

    @Column(nullable = false)
    private String admin; // 관리자 아이디

    // soft delete
    @Column(name = "deleted_at")
    private LocalDateTime deleted_at;

    public void changeGroupName(String group_name) {
        this.group_name = group_name;
    }

    // 가입 성공시에 present_member_num ++
    public void incrementMemberCount() {
        // 0902 수정 필요
        if(this.present_member_num < this.max_member_num) {
            this.present_member_num ++;
        } else {
            throw new IllegalStateException("Maximum");
        }
    }
}
