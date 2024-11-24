//package com.example.microstone.security;
//
//import com.example.microstone.domain.User;
//import com.example.microstone.repository.UserRepository;
//import com.example.microstone.util.JWTUtil;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//import java.util.Map;
//
//@Log4j2
//@Component
//public class JwtTokenProvider {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // 시크릿 키
////    private String secretKey = "12345678901234567890123456789012345678901234567890";
////    private String secretKeyString = "thisIsASecretKeyThatIsExactly48BytesForHS384AlgorithmAndItShouldWorkNow123456";
//
////    private SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
//
//    // 기존 시크릿 키 (48바이트 이상이어야 함)
//    private String secretKeyString = "thisIsASecretKeyThatIsExactly48BytesForHS384AlgorithmAndItShouldWorkNow123456";
//
//    // SecretKey 객체 생성 (Base64 인코딩 불필요)
//    private SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
//
//
//    // JWT 토큰에서 uid 추출
//    public Long getUidFromToken(String token) {
//        try {
//            log.info("Received JWT: " + token);  // 서버로 전달된 JWT 로그 출력
//
////
////            // 클레임 데이터를 준비 (예: 사용자 정보)
////            Map<String, Object> valueMap = Map.of(
////                    "user_id", "minji",
////                    "role", "USER",
////                    "email", "admin1@gmail.com"
////            );
////
////// 토큰의 유효 기간을 60분으로 설정
////            int min = 60;
////
////// 생성된 토큰
////            String generatedToken = JWTUtil.generateToken(valueMap, min); // 생성된 토큰
////
////// 클라이언트에서 전달된 토큰
////            String receivedToken = token; // 클라이언트에서 전달된 토큰
////
////// 두 토큰을 비교
////            if (generatedToken.equals(receivedToken)) {
////                log.info("The tokens match.");
////            } else {
////                log.warn("The tokens do not match.");
////            }
//
//            Claims claims = Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            log.info("Parsed claims: " + claims);  // 파싱된 클레임 출력
//
//            String user_id = claims.get("user_id", String.class);
//
//            return userRepository.findByUserId(user_id)
//                    .map(User::getUid)
//                    .orElse(null);
//
//        } catch (ExpiredJwtException e) {
//            log.error("Token expired", e);
//            return null;
//        } catch (MalformedJwtException e) {
//            log.error("Invalid token format", e);
//            return null;
//        } catch (Exception e) {
//            log.error("Token parsing error", e);
//            return null;
//        }
//    }
//}

package com.example.microstone.security;

import com.example.microstone.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Log4j2
@Component
public class JwtTokenProvider {

    @Autowired
    private UserRepository userRepository;

    // 시크릿 키 (48바이트 이상이어야 함)
    private String secretKeyString = "thisIsASecretKeyThatIsExactly48BytesForHS384AlgorithmAndItShouldWorkNow123456";

    // SecretKey 객체 생성 (Base64 인코딩 불필요)
    private SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));

//    // JWT 토큰에서 user_id 추출
////    public String getUserIdFromToken(String token) {
////        try {
////            log.info("Received JWT: " + token);  // 서버로 전달된 JWT 로그 출력
////
////            // JWT 토큰을 파싱하여 클레임 추출
////            Claims claims = Jwts.parser()
////                    .setSigningKey(secretKey)
////                    .parseClaimsJws(token)
////                    .getBody();
////
////            log.info("Parsed claims: " + claims);  // 파싱된 클레임 출력
////
////            // 클레임에서 user_id 추출
////            String user_id = claims.get("user_id", String.class);
////            log.info("Extracted user_id: " + user_id);
////
////            return user_id;  // user_id 반환

    public Long getUidFromToken(String token) {
        try {
            log.info("Received JWT: " + token);
            // JWT 토큰을 파싱하여 클레임 추출
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            log.info("Parsed claims: " + claims);  // 파싱된 클레임 출력

            Long uid = claims.get("uid", Long.class);

            return uid;

        } catch (ExpiredJwtException e) {
            log.error("Token expired", e);
            return null;
        } catch (MalformedJwtException e) {
            log.error("Invalid token format", e);
            return null;
        } catch (Exception e) {
            log.error("Token parsing error", e);
            return null;
        }
    }


}