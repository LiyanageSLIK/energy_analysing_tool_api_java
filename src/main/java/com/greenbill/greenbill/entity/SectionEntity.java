package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.dto.request.NodeRequestDto;
import com.greenbill.greenbill.enumeration.NodeType;
import com.greenbill.greenbill.enumeration.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "section")
public class SectionEntity extends NodeEntity {

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<NodeEntity> children;

    public SectionEntity(NodeRequestDto nodeRequestDto) {
        setName(nodeRequestDto.getName());
        setStatus(Status.ACTIVE);
        setNodeType(NodeType.Section);
        setFrontEndId(nodeRequestDto.getFrontEndId());

    }

    public void update(NodeRequestDto nodeRequestDto) {
        setName(nodeRequestDto.getName());
    }
}
