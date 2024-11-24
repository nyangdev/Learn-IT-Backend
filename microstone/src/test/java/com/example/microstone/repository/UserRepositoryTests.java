//package com.example.microstone.repository;
//
//import com.example.microstone.domain.Enum.Role;
//import com.example.microstone.domain.User;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.annotation.Commit;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@SpringBootTest
//@Log4j2
//public class UserRepositoryTests {
//
//    @Autowired
//    UserRepository userRepository;
//
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    @Transactional
//    public void testInsert() {
//
//        for(int i = 1; i <= 10; i++) {
//            User user = User.builder()
//                    .user_id("id..." + i)
//                    .password("1234")
//                    .name("김민쟈" + i)
//                    .nickname("냥발" + i)
//                    .phone_num("안알랴줌" + i)
//                    .email("mingdeee01@gmail.com" + i)
//                    .role(Role.USER)
//                    .build();
//
//            userRepository.save(user);
//        }
//    }
//
//    @Test
//    @Transactional
//    public void testDelete() {
//        Long uid = 12L;
//
//        userRepository.deleteById(uid);
//    }
//
//    @Test
//    @Transactional
//    public void testRead() {
//
//        // uid 10인 user data 조회
//        Long uid = 10L;
//
//        java.util.Optional<User> result = userRepository.findById(uid);
//
//        User user = result.orElseThrow();
//
//        log.info("조회 완료--------------------------------");
//        log.info(user);
//        log.info("조회 끝 ---------------------------------");
//    }
//
//    @Test
//    @Transactional
//    public void testUpdate() {
//
//        Long uid = 20L;
//
//        Optional<User> result = userRepository.findById(uid);
//
//        User user = result.orElseThrow();
//        user.changeNickname("나준성인데");
//        user.changePhoneNum("나대학원간다");
//        user.changeEmail("jsjsjs@email.com");
//
//        userRepository.save(user);
//    }
//
//    // security_config
//    // 재테스트
//    @Test
//    @Transactional
////    @Rollback(value = false)
//    public void testInsertRe() {
//        for(int i = 1; i <= 10; i++) {
//            User user = User.builder()
//                    .user_id("security" + i)
//                    // security_config
//                    .password(passwordEncoder.encode("1234"))
//                    .name("시큐리티" + i)
//                    .nickname("개발" + i)
//                    .phone_num("security-123-" + i)
//                    .email("security@gmail.com" + i)
//                    .role(Role.USER)
//                    .build();
//
//            userRepository.save(user);
//        }
//    }
//
//}
