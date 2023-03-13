package com.greenbill.greenbill.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class SectionDto extends NodeDto {
    private List<NodeDto> children;
}
