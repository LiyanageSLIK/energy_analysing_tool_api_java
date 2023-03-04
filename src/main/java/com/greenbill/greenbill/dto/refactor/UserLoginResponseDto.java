package com.greenbill.greenbill.dto.refactor;

import com.greenbill.greenbill.dto.refactor.BaseDto;
import com.greenbill.greenbill.entity.refactor.UserEntity;
import com.greenbill.greenbill.enumeration.Role;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDto extends BaseDto {

    private String firstName;
    private String lastName;
    private Role role;
    private String accessToken;
    private long aTExTime;
    private String refreshToken;

    public UserLoginResponseDto(UserEntity userEntity) {
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.role = userEntity.getRole();
        this.accessToken = userEntity.getToken().getAccessToken();
        this.refreshToken = userEntity.getToken().getRefreshToken();
    }
}
