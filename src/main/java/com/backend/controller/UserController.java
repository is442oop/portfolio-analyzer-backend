package com.backend.controller;

import java.util.List;
import java.util.regex.Pattern;

import com.backend.exception.BadCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.configuration.Constants;
import com.backend.exception.BadRequestException;
import com.backend.model.User;
import com.backend.request.CreateUserRequest;
import com.backend.response.CreateUserResponse;
import com.backend.response.FindAllUsersResponse;
import com.backend.service.abstractions.IUserService;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public FindAllUsersResponse findAll() {
        logger.error("test");

        List<User> userList = userService.findAll();

        FindAllUsersResponse response = new FindAllUsersResponse();
        response.setUserList(userList);
        return response;
    }

    @PostMapping("/users")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) {
        logger.info("username: " + request.getUsername());
        logger.info("email: " + request.getEmail());

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new BadRequestException(Constants.MESSAGE_INVALIDUSERNAME);
        }

        if (request.getEmail() == null || !Pattern.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", request.getEmail())) {
            throw new BadRequestException(Constants.MESSAGE_INVALIDEMAIL);
        }

        boolean isUsernameExist = userService.isUsernameExist(request.getUsername());
        if (isUsernameExist) {
            throw new BadCredentialsException(Constants.MESSAGE_SAMEUSERNAMEEXIST);
        }

        boolean isTcnoExist = userService.isEmailExist(request.getEmail());
        if (isTcnoExist) {
            throw new BadCredentialsException(Constants.MESSAGE_SAMEEMAILEXIST);
        }

        User user = userService.createNewUser(new User(request.getUsername(), request.getEmail()));

        CreateUserResponse response = new CreateUserResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        return response;
    }

}