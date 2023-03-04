package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.refactor.ResponseWrapper;
import com.greenbill.greenbill.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/token")
    public ResponseEntity<ResponseWrapper> login(@RequestHeader(value = "Authorization", required = true) String refreshToken) {
        try {
            var accessTokenResponseDto = tokenService.requestNewAccessToken(refreshToken);
            var responseWrapper = new ResponseWrapper(
                    accessTokenResponseDto,
                    HttpStatus.OK.value(),
                    "Success: Access Token Successfully Generated");

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        } catch (HttpClientErrorException e) {
            var responseWrapper = new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(responseWrapper);
        }
    }


}
