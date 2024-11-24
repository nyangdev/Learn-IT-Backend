package com.example.microstone.security.handler;

import com.example.microstone.dto.user.UserDTO;
import com.example.microstone.repository.UserRepository;
import com.example.microstone.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

// security_config
// 로그인 성공시 핸들러
@Log4j2
@Component
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository; // UserRepository 주입

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("------------------------------");
        log.info(authentication);
        log.info("------------------------------");

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        //token_login_config
//        Map<String, Object> claims = userDTO.getClaims();

        // 패스워드를 제외한 사용자 정보만 포함하는 새로운 Map 생성
        Map<String, Object> claims = new HashMap<>();
        Long uid = getUidByUserId(userDTO.getUser_id()); // user_id를 기반으로 uid를 가져옴
        claims.put("uid", uid);
        claims.put("user_id", userDTO.getUser_id());
        claims.put("role", userDTO.getRole());
        claims.put("email", userDTO.getEmail());
        claims.put("nickname", userDTO.getNickname());
//        claims.put("social", userDTO.getSocial());

        String accessToken = JWTUtil.generateToken(claims,60000); //10분
        // 테스트목적으로 잠깐 300분으로 연장
        String refreshToken = JWTUtil.generateToken(claims,60*24); //24시간

        claims.put("access_token", accessToken);
        claims.put("refresh_token", refreshToken);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(claims);

        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();
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
