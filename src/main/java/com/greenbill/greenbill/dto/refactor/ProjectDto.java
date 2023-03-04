package com.greenbill.greenbill.dto.refactor;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDto extends BaseDto {
    private String id;
    private String name;
    private RootDto root;
}
