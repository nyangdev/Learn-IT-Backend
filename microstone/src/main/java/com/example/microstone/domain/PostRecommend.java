package com.example.microstone.domain;


import com.example.microstone.domain.Enum.PostRecommendStatus;
import com.example.microstone.domain.Id.PostRecommendId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostRecommend {

    @EmbeddedId
    private PostRecommendId id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostRecommendStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
