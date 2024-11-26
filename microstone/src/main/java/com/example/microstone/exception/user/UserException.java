package com.example.microstone.exception.user;

// 토큰 발행시에 예외 코드들
public enum UserException {

    NOT_FOUND("NOT_FOUND", 404),
    DUPLICATE("DUPLICATE", 400),
    INVALID("INVALID", 400),
    BAD_CREDENTIALS("BAD_CREDENTIALS", 401);

    private UserTaskException userTaskException;

    UserException(String message, int code) {
        userTaskException = new UserTaskException(message, code);
    }

    public UserTaskException get() {
        return userTaskException;
    }
}