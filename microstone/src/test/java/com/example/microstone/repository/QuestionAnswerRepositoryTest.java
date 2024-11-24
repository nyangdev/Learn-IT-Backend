//package com.example.microstone.repository;
//
//import com.example.microstone.domain.Board;
//import com.example.microstone.domain.Enum.Role;
//import com.example.microstone.domain.Enum.SelectedAnswer;
//import com.example.microstone.domain.Id.QuestionAnswerId;
//import com.example.microstone.domain.Post;
//import com.example.microstone.domain.QuestionAnswer;
//import com.example.microstone.domain.User;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//@Log4j2
//public class QuestionAnswerRepositoryTest {
//
//
//        @Autowired
//        UserRepository userRepository;
//
//        @Autowired
//        BoardRepository boardRepository;
//
//        @Autowired
//        PostRepository postRepository;
//
//        @Autowired
//        QuestionAnswerRepository questionAnswerRepository;
//
//        @PersistenceContext
//        EntityManager em;
//
//        @Test
//        @Transactional
//        public void testInsert(){
//
//            User user1 = User.builder()
//                    .user_id("js_clone12312313213")
//                    .password("jsjsjs123.........")
//                    .name("김준성123...")
//                    .nickname("나준성인...데오늘부터팀장한다진짜다")
//                    .phone_num("010-5678-..1234")
//                    .email("jsispm123@naver.....com")
//                    .role(Role.USER)
//                    .build();
//
//            User user2 = User.builder()
//                    .user_id("js_clone32......1")
//                    .password("jsjsjs....321")
//                    .name("김준성321....")
//                    .nickname("나준성인데......살려줘")
//                    .phone_num("010-1234-5678....")
//                    .email("jsispm123@gmail.com.....")
//                    .role(Role.USER)
//                    .build();
//
//            em.persist(user1);
//            em.persist(user2);
//
//            // 테스트용 board 생성
//            Board board = Board.builder()
//                    .board_name("준성이 대학원가라빔 게....시판")
//                    .build();
//
//            em.persist(board);
//
//            // 테스트용 post
//            Post post1 = Post.builder()
//                    .board_id(board)
//                    .uid(user1)
//                    .title("김민지는.....")
//                    .content("최고의 팀장.....")
////                    .post_upload_date(LocalDateTime.now())
//                    .recommend_num(8)
//                    .not_recommend_num(10)
//                    .views_num(1000)
//                    .reply_num(2)
//                    .build();
//
//            Post post2 = Post.builder()
//                    .board_id(board)
//                    .uid(user2)
//                    .title("거기에 대해")
//                    .content("동의한다")
////                    .post_upload_date(LocalDateTime.now())
//                    .recommend_num(8)
//                    .not_recommend_num(10)
//                    .views_num(1000)
//                    .reply_num(2)
//                    .build();
//
//            em.persist(post1);
//            em.persist(post2);
//
//            QuestionAnswerId QAid = new QuestionAnswerId();
//            QAid.setAnswer_id(post2.getPost_id());
//            QAid.setQuestion_post_id(post1.getPost_id());
//
//            QuestionAnswer QA = QuestionAnswer.builder()
//                    .id(QAid)
//                    .selected_answer_check(SelectedAnswer.NO)
//                    .build();
//
//            em.persist(QA);
//            em.flush();
//            em.clear();
//        }
//
//}
//
