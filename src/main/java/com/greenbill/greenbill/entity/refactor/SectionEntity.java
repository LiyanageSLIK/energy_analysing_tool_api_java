package com.greenbill.greenbill.entity.refactor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "section")
@Getter
@Setter
@NoArgsConstructor
public class SectionEntity extends NodeEntity {

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<NodeEntity> children;
}
