package com.greenbill.greenbill.dto.response;

import com.greenbill.greenbill.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenResponseDto extends BaseDto {
    private String accessToken;
    private long accessTokenExpireTime;
}
