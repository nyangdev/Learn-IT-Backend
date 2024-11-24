package com.example.microstone.service;

import com.example.microstone.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    public Long getUidFromToken(String token) {

    // 'Bearer ' 접두사 제거
    String accessToken = token.replace("Bearer ", "");

    JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
    //        String user_id = jwtTokenProvider.getUserIdFromToken(accessToken);
    Long uid = jwtTokenProvider.getUidFromToken(accessToken);

        if(uid !=null)

    {
        return uid;
    } else

    {
        return null;
    }
}
}
