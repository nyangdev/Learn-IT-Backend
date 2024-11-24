package com.example.microstone.controller;

import com.example.microstone.dto.Reply.ReplyCreateRequestDTO;
import com.example.microstone.dto.Reply.ReplyPageRequestDTO;
import com.example.microstone.dto.Reply.ReplyResponseDTO;
import com.example.microstone.dto.Reply.ReplyUpdateDTO;
import com.example.microstone.dto.paging.PageResponseDTO;
import com.example.microstone.dto.post.PostResponseDTO;
import com.example.microstone.repository.ReplyRepository;
import com.example.microstone.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
public class ReplyController {
    // 댓글_민지
    private final ReplyService replyService;
    private final ReplyRepository replyRepository;

    // 댓글 달기
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createReply(@RequestBody ReplyCreateRequestDTO replyCreateRequestDTO) {

        // 유저 인증
        String user_id = SecurityContextHolder.getContext().getAuthentication().getName();

        Map<String, String> response = new HashMap<>();

        if(user_id == null) {
            response.put("result", "failure");
            response.put("message", "User is not authenticated");

            return ResponseEntity.status(401).body(response);
        }

        Long replyId = replyService.registerReply(replyCreateRequestDTO, user_id);
        response.put("result", "success");
        response.put("reply_id", replyId.toString());

        return ResponseEntity.ok(response);
    }

    // 댓글 로드 (조회)
    @PostMapping("")
    public ResponseEntity<?> pagingReply(Pageable pageable, @RequestBody ReplyPageRequestDTO requestDTO) {
        Integer page = requestDTO.getPage();
        Page<ReplyResponseDTO> replyPage;

        replyPage = replyService.getReply(requestDTO);

        PageResponseDTO pages = new PageResponseDTO(replyPage);

        return ResponseEntity.ok(pages);
    }

    // 댓글 수정
    // 이거 gpt 참고하기
    @PostMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateReply(@RequestBody ReplyUpdateDTO replyUpdateDTO) {

        // 유저 아이디 인증
        String user_id = SecurityContextHolder.getContext().getAuthentication().getName();

        Map<String, String> response = new HashMap<>();

        if(user_id == null) {
            response.put("result", "failure");
            response.put("message", "User is not authenticated");
            return ResponseEntity.status(401).body(response);
        }

        try {
            replyService.updateReply(replyUpdateDTO.getReply_id(), replyUpdateDTO, user_id);
            response.put("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "failure");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // 댓글 삭제
    // soft delete 방식
    // 일정기한이 지난 후 hard delete 스케쥴러 처리 생각해야함
    @PostMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteReply(@PathVariable("id") Long reply_id) {
        // 유저 아이디 인증
        String user_id = SecurityContextHolder.getContext().getAuthentication().getName();

        Map<String, String> response = new HashMap<>();

        if(user_id == null) {
            response.put("result", "failure");
            response.put("message", "User is not authenticated");
            return ResponseEntity.status(401).body(response);
        }

        try {
            replyService.softDeleteReply(reply_id, user_id);
            response.put("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "failure");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // 댓글 검색
//    @GetMapping("/search")
    // 댓글 검색 기능은 추후에 구현 예정
}
