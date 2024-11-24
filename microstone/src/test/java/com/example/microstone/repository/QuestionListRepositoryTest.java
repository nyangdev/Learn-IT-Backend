//package com.example.microstone.repository;
//
//import com.example.microstone.domain.Enum.RemindStatus;
//import com.example.microstone.domain.Enum.Role;
//import com.example.microstone.domain.Id.QuestionListId;
//import com.example.microstone.domain.Question;
//import com.example.microstone.domain.QuestionList;
//import com.example.microstone.domain.User;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Commit;
//
//@SpringBootTest
//@Log4j2
//public class QuestionListRepositoryTest {
//
//        @Autowired
//        QuestionRepository questionRepository;
//
//        @Autowired
//        UserRepository userRepository;
//
//        @Autowired
//        QuestionListRepository questionListRepository;
//
//        @PersistenceContext
//        EntityManager em;
//
//        @Test
//        @Transactional
//        public void testInsert2() {
//
//            // 테스트용 유저 생성
//            User maker = User.builder()
//                    .user_id("boooooo")
//                    .password("123455")
//                    .name("김민지123")
//                    .nickname("조하늘디질래123")
//                    .phone_num("010-1111-3452")
//                    .email("minjiminji123@email.com")
//                    .role(Role.USER)
//                    .build();
//
//            em.persist(maker);
//            // 테스트용 질문 생성
//            Question question = Question.builder()
//                    .maker_uid(maker)
//                    .content("우리 팀장은 누구냐")
//                    .explanation("누가봐도..")
//                    .answer("김민지 ㅋ")
//                    .build();
//
//            em.persist(question);
//
//            QuestionListId QLid = new QuestionListId();
//            QLid.setQuestion_id(question.getQuestion_id());
//            QLid.setQuestion_uid(maker.getUid());
//
//            QuestionList questionList = QuestionList.builder()
//                    .id(QLid)
//                    .remind_status(RemindStatus.NO)
//                    .build();
//
//            em.persist(questionList);
//            em.flush();
//            em.clear();
//
//        }
//}
