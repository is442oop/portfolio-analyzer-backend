package com.is442oop.backendserver.controller;


import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/")

public class HelloWorldController {
    @GetMapping("/")
    public String hello(){
        return "hello world";
    }
}
