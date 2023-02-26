package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private TokenService tokenService;

    @GetMapping("/token")
    public ResponseEntity<ResponseWrapper> login(@RequestHeader(value = "Authorization", required = true) String refreshToken) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(tokenService.requestNewAccessToken(refreshToken), HttpStatus.OK.value(), "Success: Access Token Successfully Generated"));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage()));
        }
    }


}
