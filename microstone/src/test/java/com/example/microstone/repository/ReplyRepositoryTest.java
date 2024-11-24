//package com.example.microstone.repository;
//
//import com.example.microstone.domain.Board;
//import com.example.microstone.domain.Enum.Role;
//import com.example.microstone.domain.Post;
//import com.example.microstone.domain.Reply;
//import com.example.microstone.domain.User;
//import jakarta.transaction.Transactional;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.util.Optional;
//
//
//@SpringBootTest
//@Log4j2
//public class ReplyRepositoryTest {
//
//    @Autowired
//    ReplyRepository replyRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    PostRepository postRepository;
//
//    @Autowired
//    BoardRepository boardRepository;
//
//    @Test
//    @Transactional
//    public void testInsert() {
//
//        // 테스트용 유저 생성
//        User user = User.builder()
//                .user_id("js_clone123")
//                .password("jsjsjs123")
//                .name("김준성")
//                .nickname("나준성인데오늘부터팀장한다진짜다")
//                .phone_num("010-5678-1234")
//                .email("jsispm123@naver.com")
//                .role(Role.USER)
//                .build();
//
//        userRepository.save(user);
//
//        // 테스트용 board 생성
//        Board board = Board.builder()
//                .board_name("준성이 대학원가라빔 게시판")
//                .build();
//
//        boardRepository.save(board);
//
//        // 테스트용 post
//        Post post = Post.builder()
//                .board_id(board)
//                .uid(user)
//                .title("조하늘학생은 대학원에 갑니다.")
//                .content("준성이가 이렇게 써달래요")
////                .post_upload_date(LocalDateTime.now())
//                .recommend_num(7)
//                .not_recommend_num(1)
//                .views_num(100)
//                .reply_num(0)
//                .build();
//
//        postRepository.save(post);
//
//        // 테스트용 댓글
//        for(int i = 1; i <= 10; i++) {
//            Reply reply = Reply.builder()
//                    .uid(user)
//                    .content("제발요...")
//                    .recommand_num(100)
//                    .post_id(post)
//                    .build();
//            replyRepository.save(reply);
//        }
//    }
//
//    @Test
//    @Transactional
//    public void testUpdate() {
//        Long reply_id = 1L;
//
//        Optional<Reply> reply = replyRepository.findById(reply_id);
//
//        Reply reply1 = reply.orElseThrow();
//        reply1.changeContent("수정했다인마");
//        reply1.changeContentImg("이미지경로임");
//
//        replyRepository.save(reply1);
//    }
//}
