package com.backend.response;

import lombok.Data;
@Data

public class FindUserResponse {
    private long id;
    private String username;
    private String email;
}
