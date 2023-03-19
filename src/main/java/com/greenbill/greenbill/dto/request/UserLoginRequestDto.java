package com.greenbill.greenbill.dto.request;

import com.greenbill.greenbill.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDto implements BaseDto {
    private String email;
    private String password;
}
