package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.response.UserLoginResponseDto;
import com.greenbill.greenbill.entity.SubscriptionEntity;
import com.greenbill.greenbill.entity.TokenEntity;
import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.enumeration.Status;
import com.greenbill.greenbill.repository.SubscriptionRepository;
import com.greenbill.greenbill.repository.TokenRepository;
import com.greenbill.greenbill.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public TokenEntity generateLoginToken(@NonNull UserEntity userEntity) {
        TokenEntity newToken = new TokenEntity();
        newToken.setRefreshToken(jwtUtil.generateRefreshToken(userEntity.getEmail()));
        newToken.setAccessToken(jwtUtil.generateAccessToken(userEntity.getEmail()));
        return newToken;
    }

    public UserLoginResponseDto requestNewAccessToken(@NonNull String refreshToken) throws HttpClientErrorException {
        TokenEntity token = tokenRepository.findByRefreshToken(refreshToken);
        if (token == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Token:Refresh Token not found in DB");
        }
        try {
            jwtUtil.isTokenExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            resetTokenAttributes(refreshToken);
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Token Expired:Refresh Token Expired Please login");
        }
        String userEmail = jwtUtil.extractEmail(refreshToken);
        token.setAccessToken(jwtUtil.generateAccessToken(userEmail));
        tokenRepository.save(token);
        String accessToken = token.getAccessToken();
        long expireTime = jwtUtil.extractExpiration(accessToken).getTime();

        UserEntity userEntity = userRepository.findByEmail(userEmail);
        // set subscription plan here
        UserLoginResponseDto response = new UserLoginResponseDto(userEntity);
        response.setAccessToken(accessToken);

        SubscriptionEntity subscription = subscriptionRepository.findFirstByUser_EmailAndStatus(userEmail, Status.ACTIVE);
        if (subscription == null) {
            response.setSubscriptionPlanName("UnSubscribe");
        } else {
            response.setSubscriptionPlanName(String.valueOf(subscription.getSubscriptionPlan().getName()));
        }

        return response;
    }

    public Boolean validateAccessToken(String accessToken) throws HttpClientErrorException {
        TokenEntity token = tokenRepository.findByAccessToken(accessToken);
        if (token == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Wrong Token:Access Token not found in DB");
        }
        try {
            jwtUtil.isTokenExpired(accessToken);
        } catch (ExpiredJwtException e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Token Expired:Access Token Expired Request new token using Refresh Token ");
        }
        return true;
    }

    public boolean ResetTokenAttributesByAccessToken(String accessToken) throws HttpClientErrorException {
        TokenEntity token = tokenRepository.findByAccessToken(accessToken);
        if (token == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Token:Access Token not found in DB");
        }
        token.setAccessToken("loggedOut");
        token.setRefreshToken("loggedOut");
        tokenRepository.save(token);
        return true;
    }

    public boolean resetTokenAttributes(String refreshToken) throws HttpClientErrorException {
        TokenEntity token = tokenRepository.findByRefreshToken(refreshToken);
        if (token == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Token:Refresh Token not found in DB");
        }
        token.setAccessToken("loggedOut");
        token.setRefreshToken("loggedOut");
        tokenRepository.save(token);
        return (tokenRepository.save(token) != null);
    }
}
