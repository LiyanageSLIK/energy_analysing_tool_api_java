package com.greenbill.greenbill.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenbill.greenbill.entity.ProjectEntity;
import com.greenbill.greenbill.enumerat.ProjectType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProjectReqResDto extends BaseDto{
    @NotNull
    private String name;
    @NotNull
    private ProjectType projectType;
    @JsonIgnore
    private Date lastUpdated;

    public AddProjectReqResDto(ProjectEntity project) {
        this.name= project.getName();
        this.projectType=project.getProjectType();
        this.lastUpdated=project.getLastUpdated();
    }
}
