package com.example.microstone.dto.user;

import com.example.microstone.domain.Enum.Department;
import com.example.microstone.domain.Enum.Occupation;
import com.example.microstone.domain.Enum.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

// security_config
// 회원가입 api
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserFormDTO {

    @NotNull(message = "아이디는 필수 입력사항")
    @NotBlank(message = "아이디는 필수 입력사항")
    private String user_id;

    @NotNull(message = "비밀번호는 필수 입력사항")
    @NotBlank(message = "비밀번호는 필수 입력사항")
    // 7자 이상, 13자 이하, 특수문자 자유, 숫자랑 영어 조합
    @Size(min = 7, max = 13, message = "최소 7자 이상, 13자 이하의 숫자를 입력하세요")
    // 정규식 사용
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{7,13}$", message = "비밀번호는 7자 이상, 13자 이하의 숫자와 영어 조합이어야 합니다.")
    private String password;

    @NotNull(message = "이름은 필수 입력사항")
    @NotBlank(message = "이름은 필수 입력사항")
    private String name;

    @NotNull(message = "닉네임은 필수 입력사항")
    @NotBlank(message = "닉네임은 필수 입력사항")
    private String nickname;

    @NotNull(message = "비밀번호는 필수 입력사항")
    @NotBlank(message = "비밀번호는 필수 입력사항")
    private String phone_num;

    @NotNull(message = "이메일은 필수 입력사항")
    @NotBlank(message = "이메일은 필수 입력사항")
    private String email;

//    private boolean social;


    // 0803_수정
    private String occupation;

    // 0803_수정
    private String department;

    // 0803_수정
//    private Role role;

    // security_config
    // security_sign_up
    @Builder
    public UserFormDTO(String user_id, String password, String name, String phone_num, String email, boolean social, Role role) {
        this.user_id = user_id;
        this.password = password;
        this.name = name;
        this.phone_num = phone_num;
        this.email = email;
//        this.social = social;
//        this.role = role;
    }

    public Department getDepartmentAsEnum() {
        try {
            return Department.valueOf(department); // Enum으로 변환
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 학과입니다: " + department);
        }
    }

    public Occupation getOccupationAsEnum() {
        try {
            return Occupation.valueOf(occupation); // Enum으로 변환
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 직업입니다: " + occupation);
        }
    }
}