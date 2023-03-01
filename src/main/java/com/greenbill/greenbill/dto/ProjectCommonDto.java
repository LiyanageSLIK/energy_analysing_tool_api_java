package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.enumerat.ProjectType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCommonDto extends BaseDto{

    private String name;
    private ProjectType projectType;
    private long projectId;
    private Date lastUpdated;
}
