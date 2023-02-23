package com.greenbill.greenbill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeReqDto  extends BaseDto{

    private String email;
    private String OldPassword;
    private String NewPassword;

}
