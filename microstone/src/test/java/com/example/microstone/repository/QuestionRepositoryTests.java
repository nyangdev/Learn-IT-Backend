//package com.example.microstone.repository;
//
//import com.example.microstone.domain.Enum.Role;
//import com.example.microstone.domain.Question;
//import com.example.microstone.domain.User;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@SpringBootTest
//@Log4j2
//public class QuestionRepositoryTests {
//
//    @Autowired
//    QuestionRepository questionRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Test
//    @Transactional
//    public void testInsert() {
//
//        // 테스트용 유저 생성
//        User maker = User.builder()
//                .user_id("booo")
//                .password("1234")
//                .name("김민지")
//                .nickname("조하늘디질래")
//                .phone_num("010-1111-2222")
//                .email("minjiminji@email.com")
//                .role(Role.USER)
//                .build();
//
//        userRepository.save(maker);
//
//        // 테스트용 질문 생성
//        Question question = Question.builder()
//                .maker_uid(maker)
//                .content("김민지는 키가 몇이게")
//                .explanation("민지는 키가 커보이잖아")
//                .answer("174")
//                .build();
//
//        questionRepository.save(question);
//    }
//
//    @Test
//    @Transactional
//    public void testInsert2() {
//
//        // 테스트용 유저 생성
//        User maker = User.builder()
//                .user_id("boooooo")
//                .password("123455")
//                .name("김민지123")
//                .nickname("조하늘디질래123")
//                .phone_num("010-1111-3452")
//                .email("minjiminji123@email.com")
//                .role(Role.USER)
//                .build();
//
//        userRepository.save(maker);
//
//        // 테스트용 질문 생성
//        Question question = Question.builder()
//                .maker_uid(maker)
//                .content("우리 팀장은 누구냐")
//                .explanation("누가봐도..")
//                .answer("조하늘 ㅋ")
//                .build();
//
//        questionRepository.save(question);
//    }
//
//    @Test
//    @Transactional
//    public void testFindAll() {
//        List<Question> questions = questionRepository.findAll();
//        questions.forEach(question -> log.info("Found question: " + question.getContent()));
//    }
//}
