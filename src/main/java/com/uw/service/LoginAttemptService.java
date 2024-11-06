package com.uw.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Service class for handling login attempts and blocking users after a certain number of failed attempts.
 */
@Service
public class LoginAttemptService {

    private static final Logger logger = LoggerFactory.getLogger(LoginAttemptService.class);

    private final int MAX_ATTEMPT = 3;
    private final long LOCK_TIME_DURATION = TimeUnit.MINUTES.toMillis(5);
    private Map<String, Integer> attemptsCache = new HashMap<>();
    private Map<String, Long> lockTimeCache = new HashMap<>();

    /**
     * Resets the login attempts and lock time for the given key.
     *
     * @param key the key representing the user
     */
    public void loginSucceeded(String key) {
        logger.info("Login succeeded for user: {}", key);
        attemptsCache.remove(key);
        lockTimeCache.remove(key);
    }

    /**
     * Increments the login attempts for the given key and locks the user if the maximum attempts are reached.
     *
     * @param key the key representing the user
     */
    public void loginFailed(String key) {
        logger.warn("Login failed for user: {}", key);
        int attempts = attemptsCache.getOrDefault(key, 0);
        attempts++;
        attemptsCache.put(key, attempts);
        if (attempts >= MAX_ATTEMPT) {
            logger.warn("User {} is locked due to too many failed login attempts", key);
            lockTimeCache.put(key, System.currentTimeMillis());
        }
    }

    /**
     * Checks if the user is blocked due to too many failed login attempts.
     *
     * @param key the key representing the user
     * @return true if the user is blocked, false otherwise
     */
    public boolean isBlocked(String key) {
        if (!lockTimeCache.containsKey(key)) {
            return false;
        }
        long lockTime = lockTimeCache.get(key);
        if (System.currentTimeMillis() - lockTime > LOCK_TIME_DURATION) {
            loginSucceeded(key);
            return false;
        }
        logger.info("User {} is currently blocked", key);
        return true;
    }
}