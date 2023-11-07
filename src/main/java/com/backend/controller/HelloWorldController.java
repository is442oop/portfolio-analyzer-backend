package com.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/")
    public String hello() {
        logger.info("hello world");
        return "hello world";
    }
}