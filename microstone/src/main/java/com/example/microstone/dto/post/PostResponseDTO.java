package com.example.microstone.dto.post;

import com.example.microstone.domain.Enum.Category;
import com.example.microstone.domain.Enum.PostRecommendStatus;
import com.example.microstone.domain.Image;
import com.example.microstone.domain.Post;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Data
public class PostResponseDTO {
    private long post_id;
    private String nickname;
    private String title;
    private String content;
    private List<Image> images;
    private int recommend_num;
    private int not_recommend_num;
    private int views_num;
    private int reply_num;
    private Category category;
    private LocalDateTime created_at;
    private PostRecommendStatus status;

    public PostResponseDTO(Post post){
        this.post_id = post.getPost_id();
        this.nickname = post.getUid().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.images = post.getImages();
        this.recommend_num = post.getRecommend_num();
        this.not_recommend_num = post.getNot_recommend_num();
        this.views_num = post.getViews_num();
        this.reply_num = post.getReply_num();
        this.category = post.getCategory();
        this.created_at = post.getCreatedAt();
    }
}
