package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.enumeration.NodeType;
import com.greenbill.greenbill.enumeration.Status;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class NodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "front_end_id", nullable = false)
    private String frontEndId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "node_type", nullable = false)
    private NodeType nodeType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private NodeEntity parent;
}
