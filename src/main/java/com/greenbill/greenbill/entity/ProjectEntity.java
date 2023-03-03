package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.dto.AddProjectReqResDto;
import com.greenbill.greenbill.enumerat.ProjectType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "project_name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_type", nullable = false)
    private ProjectType projectType;

    @Column(name = "last_updated", nullable = false)
    private Date lastUpdated = new Date();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TreeViewEntity treeView;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SectionEntity> sections = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private UserEntity user;

    public ProjectEntity(AddProjectReqResDto addProjectReqResDto) {
        this.name = addProjectReqResDto.getProjectName();
        this.projectType = addProjectReqResDto.getProjectType();
        this.lastUpdated = new Date();
    }

    public void update(AddProjectReqResDto addProjectReqResDto) {
        this.setName(addProjectReqResDto.getProjectName());
        this.setLastUpdated(new Date());
        this.setProjectType(addProjectReqResDto.getProjectType());

    }

    public void setLastUpdated() {
        this.lastUpdated = new Date();
    }
}
