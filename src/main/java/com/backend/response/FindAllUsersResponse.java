package com.backend.response;

import java.util.List;

import com.backend.model.User;

import lombok.Data;

@Data
public class FindAllUsersResponse {
    List<User> userList;
}
