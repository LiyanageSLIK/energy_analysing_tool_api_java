package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.enumeration.NodeType;
import com.greenbill.greenbill.enumeration.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Table(name = "root")
@Getter
@Setter
@Entity
public class RootEntity extends NodeEntity {

    @OneToOne(cascade = CascadeType.ALL)
    private ProjectEntity project;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<NodeEntity> children;

    public RootEntity() {
        setName("root");
        setParent(null);
        setFrontEndId("root");
        setNodeType(NodeType.Root);
        setStatus(Status.ACTIVE);
    }
}
