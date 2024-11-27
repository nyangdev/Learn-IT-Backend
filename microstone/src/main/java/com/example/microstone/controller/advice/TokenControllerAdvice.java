package com.example.microstone.controller.advice;

import com.example.microstone.exception.user.UserTaskException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Log4j2
public class TokenControllerAdvice {

    @ExceptionHandler(UserTaskException.class)
    public ResponseEntity<Map<String, String>> handleTaskException(UserTaskException ex) {

        log.error(ex.getMessage());

        String message = ex.getMessage();
        int status = ex.getCode();

        Map<String, String> map = Map.of("error", message);

        return ResponseEntity.status(status).body(map);
    }
}
