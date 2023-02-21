package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto extends BaseDto{
    private String email;
    private String password;

}
