package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.enumerat.Status;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddSectionDto extends BaseDto{

    private Long nodeId;
    private Long parentSectionId;
    private String name;
    private Status status;
}
