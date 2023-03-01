package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.dto.AddSectionDto;
import com.greenbill.greenbill.enumerat.Status;
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
@Table(name = "section")
public class SectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "node_id", nullable = false)
    private String nodeId;
    @Column(name = "parent_nod_id", updatable = false, nullable = true)
    private String parentNodId;
    @Column(name = "section_name", nullable = false)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "last_updated", nullable = false)
    private Date lastUpdated= new Date();

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private SectionEntity parentSection;

    @OneToMany(mappedBy = "parentSection", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SectionEntity> childSections = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private ProjectEntity project;

    @OneToMany(mappedBy = "section", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ApplianceEntity> appliances = new ArrayList<>();

    public SectionEntity(AddSectionDto addSectionDto) {
        this.nodeId = addSectionDto.getNodeId();
        this.parentNodId = addSectionDto.getParentNodId();
        this.name = addSectionDto.getName();
        this.status = addSectionDto.getStatus();
    }

    public void setLastUpdated() {
        this.lastUpdated = new Date();
    }
}
