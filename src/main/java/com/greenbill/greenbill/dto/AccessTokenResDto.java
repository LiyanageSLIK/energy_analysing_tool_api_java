package com.greenbill.greenbill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenResDto extends BaseDto {
    private String accessToken;
    private long aTExTime;
}
