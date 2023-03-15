package com.greenbill.greenbill.dto.response;

import com.greenbill.greenbill.dto.BaseDto;
import com.greenbill.greenbill.entity.ProjectEntity;
import com.greenbill.greenbill.enumeration.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSummaryDto extends BaseDto {
    private long projectId;
    private String name;
    private ProjectType projectType;
    private String lastUpdated;

    public ProjectSummaryDto(ProjectEntity projectEntity) {
        setProjectId(projectEntity.getId());
        setName(projectEntity.getName());
        setProjectType(projectEntity.getProjectType());
        setLastUpdated(projectEntity.getLastUpdated());
    }

    public void setLastUpdated(Date lastUpdated) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String istDate = sdf.format(lastUpdated);
        this.lastUpdated = istDate;
    }
}
