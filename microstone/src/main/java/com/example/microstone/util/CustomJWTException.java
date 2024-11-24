package com.example.microstone.util;

// JWT 예외처리
public class CustomJWTException extends RuntimeException {
    //token_login_config

    public CustomJWTException(String msg) {
        super(msg);
    }
}