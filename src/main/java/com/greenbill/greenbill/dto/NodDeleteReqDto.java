package com.greenbill.greenbill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NodDeleteReqDto extends BaseDto {

    private String nodId;
    private long projectId;
}
