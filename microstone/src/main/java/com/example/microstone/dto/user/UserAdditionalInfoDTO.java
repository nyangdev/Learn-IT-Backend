package com.example.microstone.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserAdditionalInfoDTO {

    @NotBlank(message = "이름은 필수 입력사항입니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력사항입니다.")
    private String nickname;

    @NotBlank(message = "전화번호는 필수 입력사항입니다.")
    private String phone_num;

    @NotNull(message = "학과는 필수 선택사항입니다.")
    private String department;

    @NotNull(message = "직업은 필수 선택사항입니다.")
    private String occupation;
}