package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.util.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResDto extends BaseDto {

    private String firstName;
    private String lastName;
    private Role role;
    private String accessToken;
    private String refreshToken;

    public UserLoginResDto(UserEntity userEntity) {
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.role = userEntity.getRole();
        this.accessToken = userEntity.getToken().getAccessToken();
        this.refreshToken = userEntity.getToken().getRefreshToken();
    }
}
