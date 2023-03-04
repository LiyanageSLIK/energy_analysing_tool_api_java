package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.dto.CommonNodReqDto;
import com.greenbill.greenbill.enumeration.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "section", uniqueConstraints = @UniqueConstraint(columnNames = {"node_id", "user_email"}))
public class SectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "node_id", nullable = false)
    private String nodId;
    @Column(name = "parent_nod_id", updatable = false, nullable = true)
    private String parentNodId;
    @Column(name = "section_name", nullable = false)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "last_updated", nullable = false)
    private Date lastUpdated = new Date();

    @Column(name = "reference_project_id", nullable = true)
    private Long referenceProjectId;
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private SectionEntity parentSection;

//    @OneToMany(mappedBy = "parentSection", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<SectionEntity> childSections = new ArrayList<>();
//
//    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    private ProjectEntity project;
//
//    @OneToMany(mappedBy = "section", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<ApplianceEntity> appliances = new ArrayList<>();

    public SectionEntity(CommonNodReqDto commonNodReqDto) {
        this.nodId = commonNodReqDto.getNodId();
        this.parentNodId = commonNodReqDto.getParentNodId();
        this.name = commonNodReqDto.getName();
        this.status = commonNodReqDto.getStatus();
        this.referenceProjectId = commonNodReqDto.getProjectId();
        this.lastUpdated = new Date();
    }

    public void updateSection(CommonNodReqDto commonNodReqDto) {
        this.name = commonNodReqDto.getName();
        this.status = commonNodReqDto.getStatus();
    }

    public void setLastUpdated() {
        this.lastUpdated = new Date();
    }
}
