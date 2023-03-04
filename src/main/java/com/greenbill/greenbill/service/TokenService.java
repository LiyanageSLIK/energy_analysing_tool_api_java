package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.refactor.response.AccessTokenResponseDto;
import com.greenbill.greenbill.entity.refactor.TokenEntity;
import com.greenbill.greenbill.entity.refactor.UserEntity;
import com.greenbill.greenbill.repository.TokenRepository;
import com.greenbill.greenbill.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
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

    public AccessTokenResponseDto requestNewAccessToken(@NonNull String refreshToken) throws HttpClientErrorException {
        String extractedRefreshToken = refreshToken.substring(7);
        TokenEntity token = tokenRepository.findByRefreshToken(extractedRefreshToken);
        if (token == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Token:RToken not found in DB");
        }
        try {
            jwtUtil.isTokenExpired(extractedRefreshToken);
        } catch (ExpiredJwtException e) {
            ResetTokenAttributesByRefreshToken(extractedRefreshToken);
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Token Expired:RToken Expired Please login");
        }
        String userEmail = jwtUtil.extractEmail(extractedRefreshToken);
        token.setAccessToken(jwtUtil.generateAccessToken(userEmail));
        tokenRepository.save(token);
        String accessToken = token.getAccessToken();
        long expireTime = jwtUtil.extractExpiration(accessToken).getTime();
        return new AccessTokenResponseDto(accessToken, expireTime);
    }

    public Boolean validateAccessToken(String accessToken) throws HttpClientErrorException {
        TokenEntity token = tokenRepository.findByAccessToken(accessToken);
        if (token == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Token:AToken not found in DB");
        }
        try {
            jwtUtil.isTokenExpired(accessToken);
        } catch (ExpiredJwtException e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Token Expired:AToken Expired Request new token using RToken ");
        }
        return true;
    }

    public boolean ResetTokenAttributesByAccessToken(String accessToken) throws HttpClientErrorException {
        TokenEntity token = tokenRepository.findByAccessToken(accessToken);
        if (token == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Token:AToken not found in DB");
        }
        token.setAccessToken("loggedOut");
        token.setRefreshToken("loggedOut");
        tokenRepository.save(token);
        return (tokenRepository.save(token) != null);
    }

    public boolean ResetTokenAttributesByRefreshToken(String refreshToken) throws HttpClientErrorException {
        TokenEntity token = tokenRepository.findByRefreshToken(refreshToken);
        if (token == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Token:AToken not found in DB");
        }
        token.setAccessToken("loggedOut");
        token.setRefreshToken("loggedOut");
        tokenRepository.save(token);
        return (tokenRepository.save(token) != null);
    }
}
