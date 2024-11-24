package com.example.microstone.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateDto {
    private Long post_id;
    private String title;
    private String Content;

    PostUpdateDto(String title, String Content) {
        this.post_id = post_id;
        this.title = title;
        this.Content = Content;
    }
}
