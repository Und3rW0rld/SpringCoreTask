package com.uw.service;

import com.uw.model.User;

import java.util.List;

public interface UserService {
    boolean validateUsername(String username);
    boolean validatePassword(String username, String password);
    User findUserByUsername (String username);
    boolean updateUser(User user);
    List<User> findAll();
}
