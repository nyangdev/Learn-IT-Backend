package com.example.microstone.controller;

import com.example.microstone.dto.user.UserDTO;
import com.example.microstone.repository.UserRepository;
import com.example.microstone.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class SocialController {

    private final UserService userService;
    private final UserRepository userRepository;


    @GetMapping("/api/user/kakao")
    public Map<String, Object> getMemberFromKakao(@RequestParam("accessToken") String accessToken) {

        log.info("access Token ");
        log.info(accessToken);

        // 0802issue 해결
        UserDTO userDTO = userService.getKakaoMember(accessToken);

        // 비활성화된 유저일 때 비활성화 취소하기
        if(userService.findByUserId(userDTO.getUser_id()).is_deactivated()) {
            userService.cancelDeactivate(userDTO.getUser_id());
        }

        Map<String, Object> claims = new HashMap<>();

        Long uid = getUidByUserId(userDTO.getUser_id()); // user_id를 기반으로 uid를 가져옴
        claims.put("uid", uid);
        claims.put("user_id", userDTO.getUser_id());
        claims.put("role", userDTO.getRole());
        claims.put("email", userDTO.getEmail());
        claims.put("nickname", userDTO.getNickname());

//        String jwtAccessToken = JWTUtil.generateToken(claims, 30000);
//        String jwtRefreshToken = JWTUtil.generateToken(claims, 60*24);
//
//        claims.put("access_token", jwtAccessToken);
//        claims.put("refresh_token", jwtRefreshToken);

        return claims;
    }

    @GetMapping("/api/user/google")
    public Map<String, Object> getMemberFromGoogle(@RequestParam("accessToken") String accessToken) {
        log.info("Google access token: " + accessToken);

        UserDTO userDTO = userService.getGoogleMember(accessToken);

        // 비활성화된 유저일 때 비활성화 취소하기
        if(userService.findByUserId(userDTO.getUser_id()).is_deactivated()) {
            userService.cancelDeactivate(userDTO.getUser_id());
        }


        Map<String, Object> claims = new HashMap<>();
        Long uid = getUidByUserId(userDTO.getUser_id()); // user_id를 기반으로 uid를 가져옴
        claims.put("uid", uid);
        claims.put("user_id", userDTO.getUser_id());
        claims.put("role", userDTO.getRole());
        claims.put("email", userDTO.getEmail());
        claims.put("nickname", userDTO.getNickname());

//        String jwtAccessToken = JWTUtil.generateToken(claims, 10);
//        String jwtRefreshToken = JWTUtil.generateToken(claims, 60 * 24);
//
//        claims.put("access_token", jwtAccessToken);
//        claims.put("refresh_token", jwtRefreshToken);

        return claims;
    }

    // user_id를 기반으로 uid를 가져오는 메서드에 로그 추가
    private Long getUidByUserId(String user_id) {
        // UserRepository를 사용하여 user_id로 사용자 정보를 조회
        return userRepository.findByUserId(user_id)
                .map(user -> {
                    log.info("User found: " + user);
                    log.info("UID found: " + user.getUid());
                    return user.getUid();
                })
                .orElse(null); // 사용자 정보를 찾지 못했을 경우 null 반환
    }
}
