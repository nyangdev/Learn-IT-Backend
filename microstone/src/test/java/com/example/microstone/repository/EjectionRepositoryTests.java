//package com.example.microstone.repository;
//
//import com.example.microstone.domain.Ejection;
//import com.example.microstone.domain.Enum.Role;
//import com.example.microstone.domain.User;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Log4j2
//public class EjectionRepositoryTests {
//
//    @Autowired
//    EjectionRepository ejectionRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Test
//    @Transactional
//    public void testInsert() {
//
//        // 테스트 유저 생성
//        User user = User.builder()
//                .user_id("testuser")
//                .password("1234")
//                .name("김민지")
//                .nickname("밍디테스터")
//                .phone_num("010-1112-2333")
//                .email("testuser@gmail.com")
//                .role(Role.USER)
//                .build();
//
//        userRepository.save(user);
//
//        // 신고 테스트
//        Ejection ejection = Ejection.builder()
//                .ejection_uid(user)
//                .ejected_reason("밍디니까요")
//                .build();
//
//        ejectionRepository.save(ejection);
//    }
//}
