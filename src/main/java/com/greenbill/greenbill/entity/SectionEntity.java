package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.dto.AddSectionDto;
import com.greenbill.greenbill.enumerat.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    @Column(name = "parent_section_id", insertable=false, updatable=false, nullable = true)
    private Long parentSectionId;
    @Column(name = "node_id", nullable = false)
    private Long nodeId;
    @Column(name = "section_name", nullable = false)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private SectionEntity parentSection;

    @OneToMany(mappedBy = "parentSection", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SectionEntity> childSections = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private ProjectEntity project;

    @OneToMany(mappedBy = "section", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ApplianceEntity> appliances = new ArrayList<>();

    public SectionEntity(AddSectionDto addSectionDto) {
        this.nodeId=addSectionDto.getNodeId();
        this.parentSectionId=addSectionDto.getParentSectionId();
        this.name=addSectionDto.getName();
        this.status=addSectionDto.getStatus();
    }
}
