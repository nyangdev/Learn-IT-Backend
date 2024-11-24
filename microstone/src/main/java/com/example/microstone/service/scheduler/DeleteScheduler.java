package com.example.microstone.service.scheduler;

import com.example.microstone.domain.Image;
import com.example.microstone.domain.Post;
import com.example.microstone.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class DeleteScheduler {

    @Autowired
    PostRepository postRepository;

    //매일 18시에 실행
    //cron = 초 분 시간 일 월 요일
    //요일(0-6 일-토)
    @Scheduled(cron = "0 33 22 * * *")
    public void deletePost(){
        List<Post> posts = postRepository.findAllDeletedPosts();
        LocalDate now = LocalDate.now();
        for (Post post : posts) {
            if(post.getDeleted_at().isBefore(now)){
                List<Image> images = post.getImages();
                for (Image image : images) {
                    File originalFile = new File(image.getFilePath());
                    originalFile.delete();
                    File ThumbnailFile = new File(image.getThumbnailFilePath());
                    ThumbnailFile.delete();
                }
                postRepository.delete(post);
            }
        }
    }
}
