package com.example.microstone.dto.user;

import com.example.microstone.domain.Enum.Department;
import com.example.microstone.domain.Enum.Occupation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private UserDTO user;

    private Occupation occupation;

    private Department department;

    private String phone_num;

    private LocalDateTime created_at;
}
