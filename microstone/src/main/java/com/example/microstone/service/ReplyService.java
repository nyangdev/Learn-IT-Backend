package com.example.microstone.service;

import com.example.microstone.domain.Enum.Category;
import com.example.microstone.domain.Post;
import com.example.microstone.domain.Reply;
import com.example.microstone.domain.User;
import com.example.microstone.dto.Reply.ReplyCreateRequestDTO;
import com.example.microstone.dto.Reply.ReplyPageRequestDTO;
import com.example.microstone.dto.Reply.ReplyResponseDTO;
import com.example.microstone.dto.Reply.ReplyUpdateDTO;
import com.example.microstone.dto.post.PostResponseDTO;
import com.example.microstone.repository.PostRepository;
import com.example.microstone.repository.ReplyRepository;
import com.example.microstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplyService {
    // 댓글_민지
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    // 댓글 달기
    // 댓글 생성
    @Transactional
    public Long registerReply(ReplyCreateRequestDTO replyCreateRequestDTO, String user_id) {

        // 댓글과 연결될 post 존재여부 확인
        // 조회
        Post post = postRepository.findById(replyCreateRequestDTO.getPost_id())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 댓글 작성 유저 조회
        User user = userRepository.findByUserId(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 댓글 생성
        Reply reply = Reply.builder()
                .post_id(post)
                .uid(user)
                .content(replyCreateRequestDTO.getContent())
                .recommand_num(0)
                .group_id(post.getGroup_id())
                .group_type(post.getGroup_type())
                .build();

        replyRepository.save(reply);

        return reply.getReply_id();
    }

    // 댓글 조회
    @Transactional
    public Page<ReplyResponseDTO> getReply(ReplyPageRequestDTO requestDTO) {
        //page = pageable.getPageNumber(); //현재 페이지 번호를 가져옵니다.
        int pageLimit = 10; // 페이지당 최대 포스트 수

        int page = requestDTO.getPage();

        Page<Reply> replyPage = null;
        Post post = postRepository.findById(requestDTO.getPost_id()).get();
        replyPage = replyRepository.findAllReply(PageRequest.of(page-1, pageLimit),post);

        // 각 Post를 PostResponseDto로 변환합니다.
        Page<ReplyResponseDTO> replyResponseDTOS = replyPage.map(reply -> new ReplyResponseDTO(reply));

        return replyResponseDTOS;


    }

    // 댓글 수정
    public void updateReply(Long reply_id, ReplyUpdateDTO replyUpdateDTO, String user_id) {
        // 수정 대상의 댓글 조회
        Optional<Reply> replyOpt = replyRepository.findById(reply_id);

        // 존재하는 댓글인지 확인
        if(!replyOpt.isPresent()) {
            throw new RuntimeException("Reply not found");
        }

        // 존재하는 댓글일때
        Reply reply = replyOpt.get();

        // 댓글 작성자가 현재 인증된 유저와 일치하는지 확인
        if(!reply.getUid().getUser_id().equals(user_id)) {
            throw new RuntimeException("User not authorized to update this reply");
        }

        // 댓글 내용 수정
        reply.changeContent(replyUpdateDTO.getContent());

        replyRepository.save(reply);
    }

    // 댓글 삭제(soft delete)
    public void softDeleteReply(Long reply_id, String user_id) {
        Optional<Reply> replyOpt = replyRepository.findById(reply_id);

        if(!replyOpt.isPresent()) {
            throw new RuntimeException("Reply not found");
        }

        Reply reply = replyOpt.get();

        if(!reply.getUid().getUser_id().equals(user_id)) {
            throw new RuntimeException("User not authorized to delete this reply");
        }

        reply.setDeleted_at(LocalDateTime.now());

        replyRepository.save(reply);
    }

    // hard delete 댓글
    @Scheduled(cron = "0 0 2 * * ?")
    public void hardDeleteReply() {
        LocalDateTime Date = LocalDateTime.now().minusDays(30); // 현재로부터 30일전

        // 삭제된지 30일 이상 지난 댓글 조회
        List<Reply> oldReply = replyRepository.findAllByDeleted_atBefore(Date);

        replyRepository.deleteAll(oldReply);
    }
}
