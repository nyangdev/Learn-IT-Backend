package com.example.microstone.dto.Reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 댓글_민지
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyUpdateDTO {
    private Long reply_id;

    private String content;

}
