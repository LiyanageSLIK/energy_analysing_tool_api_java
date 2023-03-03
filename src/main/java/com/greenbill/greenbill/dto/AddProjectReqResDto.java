package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.entity.ProjectEntity;
import com.greenbill.greenbill.enumeration.ProjectType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

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
    private String lastUpdated;




    public void setLastUpdated(Date lastUpdated) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String istDate = sdf.format(lastUpdated);
        this.lastUpdated = istDate;
    }

    public AddProjectReqResDto(ProjectEntity project) {
        this.projectName = project.getName();
        this.projectType = project.getProjectType();
        this.projectId = project.getId();
        this.setLastUpdated(project.getLastUpdated());
    }
}
