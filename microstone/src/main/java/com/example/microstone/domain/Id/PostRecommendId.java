package com.example.microstone.domain.Id;

import com.example.microstone.domain.Post;
import com.example.microstone.domain.User;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRecommendId implements Serializable {

    private Long userId;
    private Long postId;
}
