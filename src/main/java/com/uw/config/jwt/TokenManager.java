package com.uw.config.jwt;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
@Component
public class TokenManager {
    public static final Set<String> revokedTokens = new HashSet<>();

    public TokenManager() {
    }

    public void invalidateToken(String token) {
        revokedTokens.add(token);
    }

}