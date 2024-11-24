package com.example.microstone.controller;


import com.example.microstone.domain.Image;
import com.example.microstone.repository.ImageRepository;
import com.example.microstone.service.file.FileService;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;


@Log4j2
@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    // 로깅
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private ImageRepository imageRepository;


    @GetMapping("/")
    public String uploadAjax() {
        return "uploadAjax";
    }

    @ResponseBody // 객체를 json 형식으로 데이터 리턴(서버 -> 클라이언트)
    @RequestMapping(value = "/api/upload/uploadAjax/{id}" , method = RequestMethod.POST, produces = "text/plain;charset=utf-8") //한글이 깨지지 않도록 처리
    // ResponseEntity : 업로드한 파일 정보와 Http 상태 코드를 함께 리턴
    public ResponseEntity<?> uploadAjax(@RequestParam("file")MultipartFile file,@RequestParam("userId")Long userId, @PathVariable("id") Long postId) throws Exception{
        return new ResponseEntity<JSONObject>(fileService.uploadFile(file.getOriginalFilename(), file.getBytes(),userId,postId), HttpStatus.OK);

    }

    @GetMapping(value = "/files/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImageFile(@PathVariable("id") Long image_id) throws Exception {
        Image image;

        if(imageRepository.findById(image_id).isPresent()) {
            image = imageRepository.findById(image_id).get();

            InputStream is = new FileInputStream(image.getFilePath());

            byte[] bytes = is.readAllBytes();
            is.close();

            return new ResponseEntity<byte[]>(bytes, HttpStatus.OK);
        }
        return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/files/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteImage(@PathVariable("id") Long image_id) throws Exception {
        fileService.deleteImage(image_id);
        JSONObject response = new JSONObject();
        response.put("status", "success");
        response.put("message", "Image deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

