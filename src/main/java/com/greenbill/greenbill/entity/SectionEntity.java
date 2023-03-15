package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.dto.request.NodRequestDto;
import com.greenbill.greenbill.enumeration.NodeType;
import com.greenbill.greenbill.enumeration.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "section")
public class SectionEntity extends NodeEntity {

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<NodeEntity> children;

    public SectionEntity(NodRequestDto nodRequestDto) {
        setName(nodRequestDto.getName());
        setStatus(Status.ACTIVE);
        setNodeType(NodeType.Section);
        setFrontEndId(nodRequestDto.getFrontEndId());

    }

    public void update(NodRequestDto nodRequestDto) {
        setName(nodRequestDto.getName());
    }
}
