package com.example.microstone.dto.user;

import com.example.microstone.domain.Enum.Department;
import com.example.microstone.domain.Enum.Occupation;
import lombok.Data;

@Data
public class UserUpdateDTO {
    private String user_id;
    private String nickname;
    private String phone_num;
    private Occupation occupation;
    private Department department;
}