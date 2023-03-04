package com.greenbill.greenbill.dto.refactor;

import com.greenbill.greenbill.dto.refactor.BaseDto;
import com.greenbill.greenbill.entity.refactor.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto extends BaseDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UserRegisterDto(UserEntity userEntity) {
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();

    }
}
