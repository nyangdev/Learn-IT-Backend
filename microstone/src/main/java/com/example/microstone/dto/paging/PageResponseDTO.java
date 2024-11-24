package com.example.microstone.dto.paging;

import com.example.microstone.domain.Enum.Category;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class PageResponseDTO {
    private long totalElement;
    private int totalPages;
    private int currentElements;
    private List<?> content;

    public PageResponseDTO(Page page){
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.currentElements = page.getNumberOfElements() ;
        this.totalElement = page.getTotalElements();
    }
}
