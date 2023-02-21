package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.entity.TokenEntity;
import com.greenbill.greenbill.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResDto extends BaseDto{

    private String firstName;
    private String lastName;
    private String type;
    private TokenEntity token;

    public UserLoginResDto(UserEntity userEntity) {
        this.firstName= userEntity.getFirstName();
        this.lastName= userEntity.getLastName();
        this.token=userEntity.getToken();
    }
}
