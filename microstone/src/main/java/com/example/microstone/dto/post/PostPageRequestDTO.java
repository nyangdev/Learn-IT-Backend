package com.example.microstone.dto.post;

import com.example.microstone.domain.Enum.Category;
import com.example.microstone.domain.Enum.GroupType;
import lombok.Data;

@Data
public class PostPageRequestDTO {
    private int page;
    private String type;
    private String category;
    private Long group_id;

}
