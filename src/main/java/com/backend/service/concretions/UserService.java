package com.backend.service.concretions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.exception.UserNotFoundException;
import com.backend.model.User;
import com.backend.repository.UserRepository;

@Service
public class UserService implements com.backend.service.abstractions.IUserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User createNewUser(User user) {
        return repository.save(user);
    }

    @Override
    public User findByUserName(String username) {
        User user = repository.findByUsername(username);

        if (user == null)
            throw new UserNotFoundException(username);
        else
            return user;
    }

    @Override
    public User findByEmail(String email) {
        User user = repository.findByEmail(email);

        if (user == null)
            throw new UserNotFoundException("with email: " + email);
        else
            return user;
    }

    @Override
    public boolean isUsernameExist(String username) {
        User user = repository.findByUsername(username);
        return user != null;
    }

    @Override
    public boolean isEmailExist(String email) {
        User user = repository.findByEmail(email);
        return user != null;
    }

    @Override 
    public User findById(long id) {
        User user = repository.findById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        } else {
            return user;
        }
    }
}
