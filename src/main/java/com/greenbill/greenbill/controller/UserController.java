package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.refactor.UserLoginResponseDto;
import com.greenbill.greenbill.dto.refactor.request.PasswordChangeRequestDto;
import com.greenbill.greenbill.dto.refactor.ResponseWrapper;
import com.greenbill.greenbill.dto.refactor.UserLoginDto;
import com.greenbill.greenbill.dto.refactor.UserRegisterDto;
import com.greenbill.greenbill.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        try {
            var loginResponseDto = userService.login(userLoginDto);
            var successResponse = new ResponseWrapper(loginResponseDto, HttpStatus.OK.value(), "Success: Successfully loggedIn");
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (HttpClientErrorException e) {
            var errorResponse = new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> login(@RequestHeader(value = "Authorization", required = true) String token) {
        try {
            userService.logOut(token);
            return ResponseEntity.status(HttpStatus.OK).body("Success: Successfully loggedOut");
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper> register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        try {
            var registerResponseDto = userService.register(userRegisterDto);
            var successResponse = new ResponseWrapper(registerResponseDto, HttpStatus.OK.value(), "Success: Successfully Registered");
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage()));
        }
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(
            @RequestBody @Valid PasswordChangeRequestDto passwordChangeRequestDto,
            @RequestHeader(value = "Authorization", required = true) String token) {
        try {
            if (userService.changePassword(passwordChangeRequestDto, token)) {
                return ResponseEntity.status(HttpStatus.OK).body("Success: Successfully Changed Password");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("UnSuccess: Password Not Changed");
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestBody @Valid UserLoginDto userLoginDto,
            @RequestHeader(value = "Authorization", required = true) String token) {
        try {
            if (userService.delete(userLoginDto, token)) {
                return ResponseEntity.status(HttpStatus.OK).body("Success: Account Successfully Deleted");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("UnSuccess: Delete Unsuccessful");
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }


}
