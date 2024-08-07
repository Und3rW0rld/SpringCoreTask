package com.uw.dao;

import com.uw.model.User;

import java.util.List;

public interface UserDao {
    boolean validateUsername(String username);
    boolean validatePassword(String username, String password);
    User findUserByUsername(String username);
    boolean updateUser(User user);
    List<User> findAll();
}
