package com.example.microstone.dto;

import com.example.microstone.domain.Enum.RequestStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 기존 요청의 상태를 업데이트할때 사용
public class JoinRequestUpdateDTO {

    private Long request_id;

    private RequestStatus status;
}
