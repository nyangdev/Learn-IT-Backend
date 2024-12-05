package com.example.microstone.security.auth;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
public class CustomUserPrincipal implements Principal {

    private final String user_id;

    @Override
    public String getName() {
        return user_id;
    }
}
