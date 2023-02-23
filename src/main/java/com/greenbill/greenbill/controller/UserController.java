package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.PasswordChangeReqDto;
import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.dto.UserLoginDto;
import com.greenbill.greenbill.dto.UserRegisterDto;
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
    public ResponseEntity<ResponseWrapper> login(@RequestBody @Valid UserLoginDto userLoginDto,@RequestHeader(value = "Authorization", required = true) String token) {
        System.out.println("Authorization header value: " + token);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(userService.login(userLoginDto), HttpStatus.OK.value(), "Success: Successfully loggedIn"));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity login(@RequestHeader(value = "Authorization", required = true) String token) {
        try {
            userService.logOut(token);
            return ResponseEntity.status(HttpStatus.OK).body(new String("Success: Successfully loggedOut"));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper> register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(userService.register(userRegisterDto), HttpStatus.OK.value(), "Success: Successfully Registered"));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage()));
        }
    }

    @PutMapping("/password")
    public ResponseEntity changePassword(@RequestBody @Valid PasswordChangeReqDto passwordChangeReqDto, @RequestHeader(value = "Authorization", required = true) String token) {
        try {
            if(userService.changePassword(passwordChangeReqDto,token)){
                return ResponseEntity.status(HttpStatus.OK).body(new String("Success: Successfully Changed Password"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new String("UnSuccess: Password Not Changed"));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestBody @Valid UserLoginDto userLoginDto, @RequestHeader(value = "Authorization", required = true) String token) {
        try {
            if(userService.delete(userLoginDto,token)){
                return ResponseEntity.status(HttpStatus.OK).body(new String("Success: Account Successfully Deleted"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new String("UnSuccess: Delete Unsuccessful"));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }


}
