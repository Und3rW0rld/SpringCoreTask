package com.uw.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User with username '" + username + "' not found.");
    }
}
