package com.greenbill.greenbill.dto.response;

import com.greenbill.greenbill.entity.ProjectEntity;
import com.greenbill.greenbill.enumeration.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEnergyConsumptionDetailsDto {
    private long projectId;
    private String name;
    private ProjectType projectType;
    private double totalUnits;
    private List<NodeEnergyConsumptionDetailsDto> children;

    public ProjectEnergyConsumptionDetailsDto(ProjectEntity project) {
        setProjectId(project.getId());
        setName(project.getName());
        setProjectType(project.getProjectType());
    }

}
