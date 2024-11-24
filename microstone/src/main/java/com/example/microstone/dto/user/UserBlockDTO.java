package com.example.microstone.dto.user;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserBlockDTO {

    private String blockerUsername; // 차단한 유저의 사용자명

    private String blockedUsername; // 차단된 유저의 사용자명

    private LocalDateTime blockedAt; // 차단된 시간

}