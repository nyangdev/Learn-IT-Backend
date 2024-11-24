package com.example.microstone.dto.user;

import com.example.microstone.domain.Enum.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// security_config
// 로그인 api
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements UserDetails {
    // UserDetails
    // 스프링 시큐리티가 사용자의 인증을 처리하는 데 필요한 사용자 정보를 제공하는 인터페이스

    private String user_id;

    private String password;

    private String nickname;

//    private boolean social;

    private String email;

    private Role role;

    // 사용자에게 부여된 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    // 사용자명 반환
    @Override
    public String getUsername() {
        return user_id;
    }

    @Override
    public String getPassword() {
        return password;
    }

//    public Boolean getSocial(){return social;}

    //token_login_config
    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("user_id", user_id);
        dataMap.put("password", password);
        dataMap.put("nickname", nickname);
//        dataMap.put("social", social);
        dataMap.put("email", email);
        dataMap.put("role", role);

        return dataMap;
    }
}