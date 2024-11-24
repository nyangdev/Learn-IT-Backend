package com.example.microstone.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {

    private static String secretKeyString = "thisIsASecretKeyThatIsExactly48BytesForHS384AlgorithmAndItShouldWorkNow123456";

    // Base64 인코딩
//    private static String encodedSecretKeyString = Base64.getEncoder().encodeToString(secretKeyString.getBytes());

    // SecretKey 객체 생성
//    private static SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(encodedSecretKeyString));

    // 시크릿 키 생성
    private static SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());

    // JWT 문자열 생성
    public static String generateToken(Map<String, Object> valueMap, int min) {
        String jwtStr = Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant())) // 발급 시간 설정
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())) // 만료 시간 설정
                .signWith(secretKey, SignatureAlgorithm.HS384)
                .compact();

        log.info("Generated JWT: " + jwtStr);  // 토큰 생성 후 로그 출력

        return jwtStr;
    }

    public static Map<String, Object> validateToken(String token) {
        Map<String, Object> claim = null;

        try {
            claim = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomJWTException("Malformed");
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJWTException("Expired");
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomJWTException("Invalid");
        } catch (JwtException jwtException) {
            throw new CustomJWTException("JWTError");
        } catch (Exception e) {
            throw new CustomJWTException("Error");
        }

        return claim;
    }
}