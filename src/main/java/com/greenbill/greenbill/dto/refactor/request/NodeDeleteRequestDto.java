package com.greenbill.greenbill.dto.refactor.request;

import com.greenbill.greenbill.dto.refactor.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NodeDeleteRequestDto extends BaseDto {

    private String nodId;
    private long projectId;
}
