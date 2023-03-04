package com.greenbill.greenbill.dto.refactor.request;

import com.greenbill.greenbill.dto.refactor.BaseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequestDto extends BaseDto {

    private String email;
    private String oldPassword;
    private String newPassword;
}
