package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.enumerat.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddSectionDto extends BaseDto {

    private String nodeId;
    private String parentNodId;
    private Long projectId;
    private String name;
    private Status status;
}
