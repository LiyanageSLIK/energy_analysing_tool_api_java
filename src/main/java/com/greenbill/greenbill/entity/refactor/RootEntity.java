package com.greenbill.greenbill.entity.refactor;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "root")
@Getter
@Setter
@NoArgsConstructor
public class RootEntity extends NodeEntity {

    @OneToOne(mappedBy = "root", cascade = CascadeType.PERSIST)
    private ProjectEntity project;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<NodeEntity> children;
}
