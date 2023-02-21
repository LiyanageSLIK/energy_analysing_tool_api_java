package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.dto.UserLoginDto;
import com.greenbill.greenbill.dto.UserRegisterDto;
import com.greenbill.greenbill.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/login")
    public ResponseWrapper login(@RequestBody @Valid UserLoginDto userLoginDto) {
        try {
            return new ResponseWrapper(userService.login(userLoginDto),HttpStatus.OK.value(), "Success: Successfully loggedIn");
        } catch (HttpClientErrorException e) {
            return new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseWrapper register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        try {
            return new ResponseWrapper(userService.register(userRegisterDto),HttpStatus.OK.value(), "Success: Successfully Registered");
        } catch (HttpClientErrorException e) {
            return new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage());
        }
    }



}
