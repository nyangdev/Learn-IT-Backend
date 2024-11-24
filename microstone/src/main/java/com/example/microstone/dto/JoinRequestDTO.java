package com.example.microstone.dto;

import com.example.microstone.domain.Enum.RequestStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestDTO {

    private Long id;

    private Long group_id;

    private String user_id;

    // 0821
    // enum 값으로 받게됨
    // 자동으로 신청시에 PENDING 처리되는걸로 설정한다
    private RequestStatus status;

    private String message_for_join; // 가입 신청 메세지
}