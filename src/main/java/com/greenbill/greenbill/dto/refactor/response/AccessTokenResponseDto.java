package com.greenbill.greenbill.dto.refactor.response;

import com.greenbill.greenbill.dto.refactor.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenResponseDto extends BaseDto {
    private String accessToken;
    private long aTExTime;
}
