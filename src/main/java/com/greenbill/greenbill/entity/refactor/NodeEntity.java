package com.greenbill.greenbill.entity.refactor;

import com.greenbill.greenbill.enumeration.NodeType;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class NodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "node_type", nullable = false)
    private NodeType nodeType;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private NodeEntity parent;
}
