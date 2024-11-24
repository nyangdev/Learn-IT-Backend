package com.example.microstone.repository;

import com.example.microstone.domain.Enum.Role;
import com.example.microstone.domain.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Log4j2
public class UserRepositoryReTests {
    // security_config

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testInsert() {

        for(int i = 1; i <= 10; i++) {
            User user = User.builder()
                    .user_id("id..." + i)
                    .password(passwordEncoder.encode("1234"))
                    .name("김민쟈" + i)
                    .nickname("냥발" + i)
                    .phone_num("안알랴줌" + i)
                    .email("mingdeee01@gmail.com" + i)
                    .role(Role.USER)
                    .build();

            userRepository.save(user);
        }
    }
}
