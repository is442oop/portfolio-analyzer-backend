package com.backend.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateUserRequest extends BaseRequest {
    private String id;
    private String username;
    private String email;
}
