package com.example.microstone.security.filter;

import com.example.microstone.domain.Enum.Role;
import com.example.microstone.dto.user.UserDTO;
import com.example.microstone.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {
    //token_login_config

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        if(request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String path = request.getRequestURI();

        log.info("check uri............." + path);

        // user 하위 경로는 체크하지않음
        if(path.startsWith("/api/user/")) {
            return true;
        }

        if(path.startsWith("/api/password-reset")) {
            return true;
        }

        if(path.startsWith("/api/get-token")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("-----------------------JWTCheckFilter---------------------------");

        String authHeaderStr = request.getHeader("Authorization");

        try {
            String accessToken = authHeaderStr.substring(7);
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims: " + claims);

//            filterChain.doFilter(request, response);

            String user_id = (String) claims.get("user_id");
            String password = (String) claims.get("password");
            String nickname = (String) claims.get("nickname");
            String email = (String) claims.get("email");
            Role role = Role.valueOf((String) claims.get("role"));
//            Boolean social = (Boolean) claims.get("social");

//            UserDTO userDTO = new UserDTO(user_id, password, nickname, social.booleanValue(), email, role);
            UserDTO userDTO = new UserDTO(user_id, password, nickname, email, role);

            log.info("------------------------------------");
            log.info(userDTO);
            log.info(userDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDTO, password, userDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);


            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT Check Error----------------------------");
            log.error(e.getMessage());
            e.printStackTrace();
            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();
        }
    }
}