package com.greenbill.greenbill.service;

import com.greenbill.greenbill.entity.TokenEntity;
import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.repository.TokenRepository;
import com.greenbill.greenbill.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


@Service
public class TokenService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenRepository tokenRepository;


    public TokenEntity generateLoginToken(@NonNull UserEntity userEntity) {
        TokenEntity newToken = new TokenEntity();
        newToken.setRefreshToken(jwtUtil.generateRefreshToken(userEntity.getEmail()));
        newToken.setAccessToken(jwtUtil.generateAccessToken(userEntity.getEmail()));
        return newToken;
    }

    public TokenEntity requestNewAccessToken(@NonNull String refreshToken) throws HttpClientErrorException {
        TokenEntity token = tokenRepository.findByRefreshToken(refreshToken);
        if (token == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Token:RToken not found in DB");
        }
        if (jwtUtil.isTokenExpired(refreshToken)) {
            tokenRepository.deleteByRefreshToken(refreshToken);
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Token Expired:RToken Expired Please login ");
        }
        token.setAccessToken(jwtUtil.generateAccessToken(jwtUtil.extractEmail(refreshToken)));
        return tokenRepository.save(token);
    }

    public Boolean validateAccessToken(String accessToken) throws HttpClientErrorException {
        TokenEntity token = tokenRepository.findByAccessToken(accessToken);
        if (token == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Token:AToken not found in DB");
        }
        if (jwtUtil.isTokenExpired(accessToken)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Token Expired:AToken Expired Request new token using RToken ");
        }
        return true;
    }


}
