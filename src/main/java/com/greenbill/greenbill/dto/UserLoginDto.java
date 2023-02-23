package com.greenbill.greenbill.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto extends BaseDto {
    private String email;
    private String password;

}
