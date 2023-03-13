package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.enumeration.NodeType;
import com.greenbill.greenbill.enumeration.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Table(name = "root")
@Data
@Entity
public class RootEntity extends NodeEntity {

    @OneToOne(mappedBy = "root", cascade = CascadeType.ALL)
    private ProjectEntity project;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<NodeEntity> children;

    public RootEntity() {
        setParent(null);
        setFrontEndId("root");
        setNodeType(NodeType.ROOT);
        setStatus(Status.ACTIVE);
    }
}
