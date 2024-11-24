package com.example.microstone.dto.post;

import com.example.microstone.domain.Board;
import com.example.microstone.domain.Image;
import com.example.microstone.domain.Post;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// 공통 DTO
// 게시글 조회시 응답 DTO
@Getter
@NoArgsConstructor
public class PostContentResponseDTO {

    private Board board_id; // 게시판 고유 번호

    private Long uid; // 게시글 작성 유저 고유 번호

    private Long post_id; // 게시글 고유 번호
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private List<Long> images = new ArrayList<>(); // 게시글 이미지
    private int recommend_num; // 추천 수
    private int not_recommend_num; // 비추천 수
    private int views_num; // 조회수
    private int reply_num; // 댓글수
    private LocalDateTime created_at; // 생성 시간
    private LocalDateTime updated_at; // 수정 시간
    private LocalDate deleted_at; // 삭제 시간

    public PostContentResponseDTO(Post post) {
        this.board_id = post.getBoard_id();
        this.uid = post.getUid().getUid();
        this.post_id = post.getPost_id();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.recommend_num = post.getRecommend_num();
        this.not_recommend_num = post.getNot_recommend_num();
        this.views_num = post.getViews_num();
        this.reply_num = post.getReply_num();
        this.created_at = post.getCreatedAt();
        this.updated_at = post.getUpdatedAt();
        this.deleted_at = post.getDeleted_at();

        for(Image image : post.getImages()) {
            if(image != null)
                this.images.add(image.getImageId());
        }
    }
}
