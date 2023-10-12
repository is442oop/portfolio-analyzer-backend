package com.backend.service.abstractions;

import java.util.List;

import com.backend.model.User;

public interface IUserService {
    List<User> findAll();
    User createNewUser(User user);
    User findByUserName(String username);
    User findByEmail(String email);
    boolean isUsernameExist(String username);
    boolean isEmailExist(String email);
    User findById(long id);
}
