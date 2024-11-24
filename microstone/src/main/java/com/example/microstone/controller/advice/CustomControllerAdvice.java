package com.example.microstone.controller.advice;

import com.example.microstone.util.CustomJWTException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

//token_login_config
@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(CustomJWTException.class)
    protected ResponseEntity<?> handleJWTException(CustomJWTException e) {

        String msg = e.getMessage();
        return ResponseEntity.ok().body(Map.of("error", msg));
    }
}