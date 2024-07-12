package com.uw.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class BruteForceProtector {
    private final int MAX_FAILED_ATTEMPTS = 5;
    private final long BLOCK_DURATION_IN_MINUTES = 1;
    private Map<String,Integer> failedLoginAttempts = new HashMap<>();
    private Map<String,Long> blockedUsers = new HashMap<>();

    public void blockUser(String username){
        blockedUsers.put(username, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(BLOCK_DURATION_IN_MINUTES));
        failedLoginAttempts.remove(username);
    }
    public boolean isUserBlocked(String username){
        Long blockEndTime = blockedUsers.get(username);
        return blockEndTime != null && blockEndTime > System.currentTimeMillis();
    }
    public void recordFailedLoginAttempt(String username){
        int attempts = failedLoginAttempts.getOrDefault(username,0)+1;
        failedLoginAttempts.put(username,attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS){
            blockUser(username);
        }
    }
    public void unblockUser(String username) {
        failedLoginAttempts.remove(username);
        blockedUsers.remove(username);
    }
}