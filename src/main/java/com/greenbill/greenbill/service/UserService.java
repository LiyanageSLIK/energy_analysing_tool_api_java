package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.PasswordChangeReqDto;
import com.greenbill.greenbill.dto.UserLoginDto;
import com.greenbill.greenbill.dto.UserLoginResDto;
import com.greenbill.greenbill.dto.UserRegisterDto;
import com.greenbill.greenbill.entity.SubscriptionEntity;
import com.greenbill.greenbill.entity.SubscriptionPlanEntity;
import com.greenbill.greenbill.entity.TokenEntity;
import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.enumerat.SubscriptionPlan;
import com.greenbill.greenbill.repository.SubscriptionPlanRepository;
import com.greenbill.greenbill.repository.SubscriptionRepository;
import com.greenbill.greenbill.repository.UserRepository;
import com.greenbill.greenbill.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtUtil jwtUtil;


    @Transactional
    public UserLoginResDto login(UserLoginDto userLoginDto) throws HttpClientErrorException {
        String email = userLoginDto.getEmail();
        String password = userLoginDto.getPassword();
        if (email == null || password == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Empty Input Field:Please Enter Username & Password ");
        }
        UserEntity user = (UserEntity) loadUserByUsername(email);
        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Email:User not found");
        }
        if (user.checkPassword(password)) {
            TokenEntity existingToken = user.getToken();
            TokenEntity generatedToken = tokenService.generateLoginToken(user);
            if (existingToken != null) {
                existingToken.setAccessToken(generatedToken.getAccessToken());
                existingToken.setRefreshToken(generatedToken.getRefreshToken());
                user.setToken(existingToken);
                userRepository.save(user);
            } else {
                user.setToken(generatedToken);
                userRepository.save(user);
            }
            return new UserLoginResDto(user);
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE, "Wrong Password:Enter Correct Password");
        }
    }

    @Transactional
    public UserLoginResDto register(UserRegisterDto userRegisterDto) throws HttpClientErrorException {
        UserEntity user = (UserEntity) loadUserByUsername(userRegisterDto.getEmail());
        if (user != null) {
            throw new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE, "Existing Email:Email already registered");
        }
        UserEntity newUser = new UserEntity(userRegisterDto);
        SubscriptionPlanEntity initialPlan = subscriptionPlanRepository.findByName(SubscriptionPlan.FREE);
        SubscriptionEntity initialSubscription = new SubscriptionEntity();
        initialSubscription.setSubscriptionPlan(initialPlan);
        newUser.setToken(tokenService.generateLoginToken(newUser));
        initialSubscription.setUser(userRepository.save(newUser));
        subscriptionRepository.save(initialSubscription);
        return new UserLoginResDto(newUser);
    }

    public boolean logOut(String token) throws HttpClientErrorException {
        String accessToken = token.substring(7);
        if (accessToken == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE, "Token is empty");
        }
        return tokenService.ResetTokenAttributesByAccessToken(accessToken);
    }

    public boolean changePassword(PasswordChangeReqDto passwordChangeReqDto, String token) throws HttpClientErrorException {
        String accessToken = token.substring(7);
        if (accessToken == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE, "Token is empty");
        }
        String userEmail = passwordChangeReqDto.getEmail();
        String tokenEmail = jwtUtil.extractEmail(accessToken);
        if (!userEmail.equals(tokenEmail)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Conflict With Email");
        }
        String userOldPassword = passwordChangeReqDto.getOldPassword();
        String userNewPassword = passwordChangeReqDto.getNewPassword();
        UserEntity user = (UserEntity) loadUserByUsername(userEmail);
        if (!user.checkPassword(userOldPassword)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Conflict: Old Password is wrong");
        }
        user.setPassword(userNewPassword);
        tokenService.ResetTokenAttributesByAccessToken(accessToken);
        return (userRepository.save(user).checkPassword(userNewPassword));
    }


    public boolean delete(UserLoginDto userLoginDto, String token) throws HttpClientErrorException {
        String accessToken = token.substring(7);
        if (accessToken == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE, "Token is empty");
        }
        String userEmail = userLoginDto.getEmail();
        String tokenEmail = jwtUtil.extractEmail(accessToken);
        if (!userEmail.equals(tokenEmail)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Conflict With Email");
        }
        String userPassword = userLoginDto.getPassword();
        UserEntity user = (UserEntity) loadUserByUsername(userEmail);
        if (!user.checkPassword(userPassword)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Conflict:Password is wrong");
        }
        userRepository.delete(user);
        return (loadUserByUsername(userEmail) == null);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }
}
