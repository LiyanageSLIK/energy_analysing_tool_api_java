package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.request.PasswordChangeRequestDto;
import com.greenbill.greenbill.dto.request.UserLoginRequestDto;
import com.greenbill.greenbill.dto.request.UserRegisterDto;
import com.greenbill.greenbill.dto.response.UserLoginResponseDto;
import com.greenbill.greenbill.entity.SubscriptionEntity;
import com.greenbill.greenbill.entity.TokenEntity;
import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.enumeration.Status;
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
    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto) throws HttpClientErrorException {
        String email = userLoginRequestDto.getEmail();
        String password = userLoginRequestDto.getPassword();
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
            UserLoginResponseDto response = new UserLoginResponseDto(user);
            response.setAccessTokenExpireTime(jwtUtil.extractExpiration(user.getToken().getAccessToken()).getTime());
            SubscriptionEntity subscription = subscriptionRepository.findFirstByUser_EmailAndStatus(email, Status.ACTIVE);
            if (subscription == null) {
                response.setSubscriptionPlanName("UnSubscribe");
            } else {
                response.setSubscriptionPlanName(String.valueOf(subscription.getSubscriptionPlan().getName()));
            }
            // refresh token should not be returned here
            return response;
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE, "Wrong Password:Enter Correct Password");
        }
    }

    @Transactional
    public UserLoginResponseDto register(UserRegisterDto userRegisterDto) throws HttpClientErrorException {
        UserEntity user = (UserEntity) loadUserByUsername(userRegisterDto.getEmail());
        if (user != null) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Email already registered");
        }
        UserEntity newUser = new UserEntity(userRegisterDto);
        //TODO: why initial subscription plan is not required here ???
//        SubscriptionPlanEntity initialPlan = subscriptionPlanRepository.findByName(SubscriptionPlanName.FREE);
//        SubscriptionEntity initialSubscription = new SubscriptionEntity();
//        initialSubscription.setSubscriptionPlan(initialPlan);
        newUser.setToken(tokenService.generateLoginToken(newUser));
        userRepository.save(newUser);
//        initialSubscription.setUser(userRepository.save(newUser));
//        subscriptionRepository.save(initialSubscription);
        UserLoginResponseDto response = new UserLoginResponseDto(newUser);
        response.setAccessTokenExpireTime(jwtUtil.extractExpiration(newUser.getToken().getAccessToken()).getTime());

        //TODO: why search for subscription here
        SubscriptionEntity subscription = subscriptionRepository.findFirstByUser_EmailAndStatus(newUser.getEmail(), Status.ACTIVE);
        if (subscription == null) {
            //TODO: why use this name ???
            response.setSubscriptionPlanName("UnSubscribe");
        } else {
            response.setSubscriptionPlanName(String.valueOf(subscription.getSubscriptionPlan().getName()));
        }
        return response;
    }

    public boolean logOut(String token) throws HttpClientErrorException {
        String accessToken = token.substring(7);
        if (accessToken == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE, "Token is empty");
        }
        return tokenService.ResetTokenAttributesByAccessToken(accessToken);
    }

    public boolean changePassword(PasswordChangeRequestDto passwordChangeRequestDto, String token) throws HttpClientErrorException {
        String accessToken = token.substring(7);
        if (accessToken == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE, "Token is empty");
        }
        String userEmail = passwordChangeRequestDto.getEmail();
        String tokenEmail = jwtUtil.extractEmail(accessToken);
        if (!userEmail.equals(tokenEmail)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Conflict With Email");
        }
        String userOldPassword = passwordChangeRequestDto.getOldPassword();
        String userNewPassword = passwordChangeRequestDto.getNewPassword();
        UserEntity user = (UserEntity) loadUserByUsername(userEmail);
        if (!user.checkPassword(userOldPassword)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Conflict: Old Password is wrong");
        }
        user.setPassword(userNewPassword);
        tokenService.ResetTokenAttributesByAccessToken(accessToken);
        return (userRepository.save(user).checkPassword(userNewPassword));
    }


    public boolean delete(UserLoginRequestDto userLoginRequestDto, String token) throws HttpClientErrorException {
        String accessToken = token.substring(7);
        if (accessToken == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE, "Token is empty");
        }
        String userEmail = userLoginRequestDto.getEmail();
        String tokenEmail = jwtUtil.extractEmail(accessToken);
        if (!userEmail.equals(tokenEmail)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Conflict With Email");
        }
        String userPassword = userLoginRequestDto.getPassword();
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
