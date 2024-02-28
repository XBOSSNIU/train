package com.xboss.train.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class memberController {
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
