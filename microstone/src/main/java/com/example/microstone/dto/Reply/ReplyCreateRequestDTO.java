package com.example.microstone.dto.Reply;

import lombok.*;

// 댓글_민지
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReplyCreateRequestDTO {
    // 댓글 생성시 필요한 정보를 담고있는 DTO

    private Long post_id;

    private String content;
}