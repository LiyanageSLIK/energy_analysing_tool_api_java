package com.greenbill.greenbill.dto.refactor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProjectDto extends BaseDto {
    private String id;
    private String name;
    private List<NodeDto> children;
}
