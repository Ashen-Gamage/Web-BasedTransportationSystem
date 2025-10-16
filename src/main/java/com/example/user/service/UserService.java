package com.example.user.service;

import com.example.user.dao.UserDao;
import com.example.user.model.User;

import java.sql.SQLException;

public class UserService {
    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public void registerUser(User user) throws SQLException {
        // Add validation (e.g., check if email already exists)
        User existingUser = userDao.findUserByEmail(user.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        userDao.saveUser(user);
    }

    public User loginUser(String email, String password) throws SQLException {
        if (userDao.validateUser(email, password)) {
            return userDao.findUserByEmail(email);
        }
        throw new IllegalArgumentException("Invalid email or password");
    }
}