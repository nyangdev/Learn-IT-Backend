package com.example.microstone.dto.post;

import lombok.Data;
import lombok.Getter;

@Getter
public class PostRecommendRequestDTO {
    private Long post_id;
    private String recommendation;
}
