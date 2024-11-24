package com.example.microstone.dto;

import com.example.microstone.domain.Enum.GroupRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDTO {
    private String userId;
    private String nickname;
    private String role;
    private Long groupId;
}
