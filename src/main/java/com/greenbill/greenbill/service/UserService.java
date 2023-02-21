package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.UserLoginDto;
import com.greenbill.greenbill.dto.UserLoginResDto;
import com.greenbill.greenbill.dto.UserRegisterDto;
import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.repository.UserRepository;
import jakarta.validation.Valid;
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
    private TokenService tokenService;




    public UserLoginResDto login(UserLoginDto userLoginDto)throws HttpClientErrorException{
        String email= userLoginDto.getEmail();
        String password= userLoginDto.getPassword();
        if (email==null||password==null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Empty Input Field:Please Enter Username & Password ");
        }
        UserEntity user= (UserEntity) loadUserByUsername(email);
        if (user==null){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Email:User not found");
        }
        if(user.checkPassword(password)) {
            user.setToken(tokenService.generateLoginToken(user));
            userRepository.save(user);
            return new UserLoginResDto(user);
        }else {
            throw new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE, "Wrong Password:Enter Correct Password");
        }
    }

    public UserLoginResDto register (@Valid UserRegisterDto userRegisterDto)throws HttpClientErrorException{
        UserEntity user= (UserEntity) loadUserByUsername(userRegisterDto.getEmail());
        if (user!=null){
            throw new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE, "Existing Email:Email already registered");
        }
        UserEntity newUser=new UserEntity(userRegisterDto);
        newUser.setToken(tokenService.generateLoginToken(newUser));
        userRepository.save(newUser);
        return new UserLoginResDto(newUser);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }
}
