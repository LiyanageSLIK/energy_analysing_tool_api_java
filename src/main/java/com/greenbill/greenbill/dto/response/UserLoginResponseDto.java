package com.greenbill.greenbill.dto.response;

import com.greenbill.greenbill.dto.BaseDto;
import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDto extends BaseDto {

    private long userId;
    private String firstName;
    private String lastName;
    private Role role;
    private String accessToken;
    private long accessTokenExpireTime;
    private String refreshToken;

    public UserLoginResponseDto(UserEntity userEntity) {
        this.userId = userEntity.getId();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.role = userEntity.getRole();
        this.accessToken = userEntity.getToken().getAccessToken();
        this.refreshToken = userEntity.getToken().getRefreshToken();
    }
}
