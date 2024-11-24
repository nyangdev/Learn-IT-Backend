//package com.example.microstone.repository;
//
//import com.example.microstone.domain.Enum.Role;
//import com.example.microstone.domain.Id.UserBlockId;
//import com.example.microstone.domain.User;
//import com.example.microstone.domain.UserBlock;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Log4j2
//public class UserBlockRepositoryTests {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    UserBlockRepository userBlockRepository;
//
//    // Transactional 어노테이션 주석 처리하니까 실행됨.. 이유를 모름
//    // @Transactional
//    @Test
//    @Transactional
//    public void testInsert() {
//
//        // 테스트용 유저 생성
//
//        // 블락한 유저 UID
//        User user1 = User.builder()
//                .user_id("user111111")
//                .password("1234")
//                .name("조하늘1")
//                .nickname("유저11")
//                .phone_num("010-4061-9791")
//                .email("user1@email.com")
//                .role(Role.USER)
//                .build();
//
//        userRepository.save(user1);
//
//        // 블락된 유저 UID
//        User user2 = User.builder()
//                .user_id("user222221")
//                .password("1234")
//                .name("신유림1")
//                .nickname("유저22")
//                .phone_num("010-4993-1432")
//                .email("user2@email.com")
//                .role(Role.USER)
//                .build();
//
//        userRepository.save(user2);
//
//        // 신고 테스트
//        UserBlockId id = new UserBlockId(user1.getUid(), user2.getUid());
//        UserBlock userBlock = UserBlock.builder()
//                .id(id)
//                .uid(user1)
//                .blocked_uid(user2)
//                .build();
//        userBlockRepository.save(userBlock);
//    }
//}