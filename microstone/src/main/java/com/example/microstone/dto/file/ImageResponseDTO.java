package com.example.microstone.dto.file;

import com.example.microstone.domain.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageResponseDTO {
    private Long imageId;
    private String filePath;
    private String thumbnailFilePath;


    public ImageResponseDTO(Image image) {
        this.imageId = image.getImageId();
        this.filePath = image.getFilePath();
        this.thumbnailFilePath = image.getThumbnailFilePath();
    }
}
