package com.example.user.service;

import com.example.user.dao.UserDao;
import com.example.user.model.User;

import java.sql.SQLException;

public class UserService {
    private  UserDao userDao = new UserDao();

    public int registerUser(User user) throws SQLException {
        // Add any validation here (e.g., check if email exists)
        if (userDao.findUserByEmail(user.getEmail()) != null) {
            throw new SQLException("Email already registered");
        }
        userDao.saveUser(user);
        return 0;
    }


    public User loginUser(String email, String password) throws SQLException {
        if (userDao.validateUser(email, password)) {
            return userDao.findUserByEmail(email);
        }
        return null;
    }
    public void updateUserProfile(User user) throws SQLException {
        userDao.updateUserProfile(user);
    }

    public User findUserByEmail(String email) throws SQLException {
        // Use the DAO to find the user by email
        return userDao.findUserByEmail(email);
    }

    public void deleteProfileByEmail(String email) throws SQLException {
        int deleted = userDao.deleteUserByEmail(email);
        if (deleted == 0) {
            throw new SQLException("Delete failed - user not found.");
        }
    }

    public void createUser(User user) throws SQLException {
        // Example using UserDAO
        UserDao dao = new UserDao();
        dao.createUser(user); // insert into users table
    }

    // *** ADD THIS NEW METHOD ***
    public int registerUserWithDriver(User user, String licenseNumber, String vehicleType) throws SQLException {
        return userDao.createUserWithDriver(user, licenseNumber, vehicleType);
    }

}