package com.example.microstone.exception.user;

import lombok.Getter;

@Getter
public class UserTaskException {
    // user 관련된 모든 예외 의미
    private String message;
    private int code;

    public UserTaskException(String message, int code) {
        this.message = message;
        this.code = code;
    }
}