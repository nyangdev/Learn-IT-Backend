package com.example.microstone.dto.studygroup;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyGroupDTO {

    private Long group_id;

    private String group_name;

    private int max_member_num;

    private String admin; // 관리자 id
}