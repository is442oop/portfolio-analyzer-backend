package com.backend.service.abstractions;

import java.util.List;

import com.backend.model.Portfolio;
import com.backend.model.User;

public interface IUserService {
    List<User> findAll();
    User createNewUser(User user);
    User findByUserName(String username);
    User findByEmail(String email);
    boolean isUserIdExist(String id);
    boolean isUsernameExist(String username);
    boolean isEmailExist(String email);
    User findById(String id);
    List<Portfolio> findUserPortfolios(String id);
}
