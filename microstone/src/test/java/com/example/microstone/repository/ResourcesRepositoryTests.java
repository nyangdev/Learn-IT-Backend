//package com.example.microstone.repository;
//
//import com.example.microstone.domain.Enum.FileType;
//import com.example.microstone.domain.Enum.Role;
//import com.example.microstone.domain.Resources;
//import com.example.microstone.domain.User;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Commit;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Log4j2
//public class ResourcesRepositoryTests {
//
//    @Autowired
//    ResourcesRepository resourcesRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Test
//    @Transactional
//    public void testInsert() {
//
//        // 테스트용 유저 생성
//        User user = User.builder()
//                .user_id("mj_clone12345")
//                .password("1234")
//                .name("김냥발")
//                .nickname("냥발코어")
//                .phone_num("010-123123-12324123")
//                .email("nyangdevvvvv@naver.com")
//                .role(Role.USER)
//                .build();
//
//        userRepository.save(user);
//
//        // 테스트 resources
//        Resources resources = Resources.builder()
//                .file_type(FileType.BOOK)
//                .file_title("객체지향 설계")
//                .build();
//
//        resourcesRepository.save(resources);
//    }
//}
