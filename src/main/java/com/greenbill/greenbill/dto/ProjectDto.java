package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.entity.ProjectEntity;
import com.greenbill.greenbill.entity.RootEntity;
import com.greenbill.greenbill.enumeration.ProjectType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Data
@NoArgsConstructor
public class ProjectDto implements BaseDto {

    private long projectId;
    @NotNull
    private String name;
    @NotNull
    private ProjectType projectType;
    private String lastUpdated;
    private RootDto root;

    public ProjectDto(ProjectEntity projectEntity) {
        setProjectId(projectEntity.getId());
        setName(projectEntity.getName());
        setProjectType(projectEntity.getProjectType());
        setLastUpdated(projectEntity.getLastUpdated());
        setRoot(projectEntity.getRoot());
    }

    public void setLastUpdated(Date lastUpdated) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String istDate = sdf.format(lastUpdated);
        this.lastUpdated = istDate;
    }

    public void setRoot(RootEntity rootEntity) {
        this.root = new RootDto(rootEntity);
    }
}
