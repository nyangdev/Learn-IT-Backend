package com.example.microstone.security;

import com.example.microstone.domain.User;
import com.example.microstone.dto.user.UserDTO;
import com.example.microstone.dto.user.UserFormDTO;
import com.example.microstone.repository.UserRepository;
import com.example.microstone.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// security_config
@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {

        // 동작 확인
        log.info("----------------loadUserByUsername--------------");

        // 로그인
        User user = userRepository.findByUser_id(user_id);

        if(user == null) {
            throw new UsernameNotFoundException("Not Found");
        }

        // 비활성화된 유저일때 비활성화 취소하기
        if(user.is_deactivated()) {
            userService.cancelDeactivate(user_id);
        }

        UserDTO userDTO = new UserDTO(
                user.getUser_id(),
                user.getPassword(),
                user.getNickname(),
//                user.isSocial(),
                user.getEmail(),
                user.getRole()
        );

        log.info("자격증명중....");
        log.info(userDTO);

        return userDTO;
    }

}
