package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.entity.ProjectEntity;
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
public class AddProjectReqResDto extends BaseDto {
    @NotNull
    private String projectName;
    @NotNull
    private ProjectType projectType;
    private long projectId;
    private Date lastUpdated;

    public AddProjectReqResDto(ProjectEntity project) {
        this.projectName = project.getName();
        this.projectType = project.getProjectType();
        this.projectId=project.getId();
        this.lastUpdated = project.getLastUpdated();
    }
}
