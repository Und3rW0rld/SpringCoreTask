package com.uw.service;

import com.uw.dao.UserDao;
import com.uw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean validateUsername(String username) {
        return userDao.validateUsername(username);
    }

    @Override
    public boolean validatePassword(String username, String password) {
        return userDao.validatePassword(username, password);
    }

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public boolean updateUser(User user) {
        return userDao.updateUser(user);
    }
}
