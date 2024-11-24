package com.example.microstone.config;

import com.example.microstone.security.filter.JWTCheckFilter;
import com.example.microstone.security.handler.APILoginFailHandler;
import com.example.microstone.security.handler.APILoginSuccessHandler;
import com.example.microstone.security.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// security_config
// 시큐리티 config 클래스 생성
@Configuration
@EnableWebSecurity
@Log4j2
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final APILoginSuccessHandler loginSuccessHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 시큐리티 실행 확인 로그
        log.info("------------------security config------------------");

        // CORS 설정 활성화
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.
                    configurationSource(corsConfigurationSource());
        });

        // API 서버는 무상태를 기본으로 사용하기 때문에 서버 내부에서 세션을 생성하지 않도록 하는게 일반적이지만.. 미안하게됐다
        //token_login_config
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1));

        // API 서버 작성시 CSRF 토큰을 사용하지 않는 것이 일반적
        http.csrf(config -> config.disable());

        // 로그인 연결
        http.formLogin(config -> {
            config.loginPage("/api/user/signin")
                    .usernameParameter("user_id")
                    .passwordParameter("password")
                    .successHandler(loginSuccessHandler)
                    .failureHandler(new APILoginFailHandler());
        });


        // JWT 체크
        //token_login_config
        http.addFilterBefore(new JWTCheckFilter(),
                UsernamePasswordAuthenticationFilter.class);

        //token_login_config
        http.exceptionHandling(config -> {
            config.accessDeniedHandler(new CustomAccessDeniedHandler());
        });

        http.headers(headers -> headers
                .httpStrictTransportSecurity().disable());


        return http.build();
    }

    // CORS 설정 메서드
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
