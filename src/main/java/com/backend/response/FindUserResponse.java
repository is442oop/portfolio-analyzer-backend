package com.backend.response;

import java.util.List;

import com.backend.model.Portfolio;

import lombok.Data;
@Data

public class FindUserResponse {
    private String id;
    private String username;
    private String email;
    List<Portfolio> portfolioList;
}
