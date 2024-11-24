package com.example.microstone.dto.Reply;

import com.example.microstone.domain.Post;
import com.example.microstone.domain.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 댓글_민지
// 댓글 조회시 응답 DTO
@Getter
@NoArgsConstructor
public class ReplyResponseDTO {

    private Long post_id; // post 고유 번호
    private String user_id; // 댓글 작성 유저 고유 번호

    // 닉네임
    private String nickname;

    private Long reply_id; // 댓글 고유 번호
    private String content; // 댓글 내용
    private int recommand_num; // 댓글 추천수
    private LocalDateTime created_at; // 생성 시간
    private LocalDateTime updated_at; // 수정 시간
    private LocalDateTime deleted_at; // 삭제 시간

    public ReplyResponseDTO(Reply reply) {
        this.post_id = reply.getPost_id().getPost_id();
        this.user_id = reply.getUid().getUser_id();

        // 닉네임
        this.nickname = reply.getUid().getNickname();

        this.reply_id = reply.getReply_id();
        this.content = reply.getContent();
        this.recommand_num = reply.getRecommand_num();
        this.created_at = reply.getCreatedAt();
        this.updated_at = reply.getUpdatedAt();
        this.deleted_at = reply.getDeleted_at();
    }
}