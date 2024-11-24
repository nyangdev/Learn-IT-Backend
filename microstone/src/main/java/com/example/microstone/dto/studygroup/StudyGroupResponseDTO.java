package com.example.microstone.dto.studygroup;

import com.example.microstone.domain.StudyGroup;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyGroupResponseDTO {

    private Long group_id;

    private String group_name;

    private String admin_nickname;

    private int present_member_num;

    private int max_member_num;

    public StudyGroupResponseDTO(StudyGroup studyGroup, String adminNickname) {
        this.group_id = studyGroup.getGroup_id();
        this.group_name = studyGroup.getGroup_name();
        this.admin_nickname = adminNickname; // 관리자의 닉네임을 받음
        this.present_member_num = studyGroup.getPresent_member_num();
        this.max_member_num = studyGroup.getMax_member_num();
    }
}
