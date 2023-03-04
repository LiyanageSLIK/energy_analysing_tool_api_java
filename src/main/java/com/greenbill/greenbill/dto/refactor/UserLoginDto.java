package com.greenbill.greenbill.dto.refactor;

import com.greenbill.greenbill.dto.refactor.BaseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto extends BaseDto {
    private String email;
    private String password;
}
