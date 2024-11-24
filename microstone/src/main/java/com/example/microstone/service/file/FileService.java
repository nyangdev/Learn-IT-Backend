package com.example.microstone.service.file;


import com.example.microstone.domain.Image;
import com.example.microstone.domain.Post;
import com.example.microstone.repository.ImageRepository;
import com.example.microstone.repository.PostRepository;
import com.example.microstone.repository.UserRepository;
import com.example.microstone.util.UploadFileUtils;
import com.example.microstone.config.FileConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class FileService {

    @Autowired
    private FileConfig fileConfig;

    String rootPath = fileConfig.rootPath;

    @Autowired
    final ImageRepository imageRepository;

    UploadFileUtils uploadFileUtils;
    JSONObject fileInfo;
    @Autowired
    private PostRepository postRepository;


    @Autowired
    private UserRepository userRepository;


    @Transactional
    public JSONObject uploadFile(String originalName, byte[] fileData, Long userId, Long post_id) throws Exception {

        JSONObject response = new JSONObject();
        Long id = null;
        fileInfo = uploadFileUtils.uploadFile(rootPath, originalName, fileData);
        if (fileInfo.get("response").toString() == "200") {
            Post input_post = postRepository.findById(post_id).orElseThrow(null);
            Image image = Image.builder()
                    .imageId(null)
                    .originalFileName(originalName)
                    .fileName(fileInfo.get("uploadFileName").toString())
                    .filePath(fileInfo.get("finalUploadPath").toString())
                    .thumbnailFileName(fileInfo.get("thumbnailName").toString())
                    .thumbnailFilePath(fileInfo.get("thumbnailPath").toString())
                    .post(input_post)
                    .build();

            imageRepository.save(image);

            Optional<Post> post = postRepository.findById(post_id);
            post.get().getImages().add(image);

            id = imageRepository.findCurrentId();
            response.put("FileType", "image");
            response.put("id", id);
            response.put("response", "200");
            return response;
        } else {
            response.put("response", "415");
            return response;
        }

    }

    @Transactional
    public void deleteImage(Long image_id) {
        File originalFile = new File(imageRepository.findById(image_id).get().getFilePath());
        File ThumbnailFile = new File(imageRepository.findById(image_id).get().getThumbnailFilePath());
        originalFile.delete();
        ThumbnailFile.delete();
        imageRepository.deleteById(image_id);
    }
}
