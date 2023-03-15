package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.entity.ApplianceEntity;
import com.greenbill.greenbill.entity.NodeEntity;
import com.greenbill.greenbill.entity.SectionEntity;
import com.greenbill.greenbill.enumeration.NodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionDto extends NodeDto {

    private List<NodeDto> children;

    public SectionDto(SectionEntity sectionEntity) {
        setId(sectionEntity.getId());
        setFrontEndId(sectionEntity.getFrontEndId());
        setName(sectionEntity.getName());
        setNodeType(sectionEntity.getNodeType());
        setChildren(sectionEntity.getChildren());
    }

    public void setChildren(List<NodeEntity> children) {
        List<NodeDto> childrenDto = new ArrayList<>();
        for (var nodEntity : children) {
            if (nodEntity.getNodeType() == NodeType.Section) {
                childrenDto.add(new SectionDto((SectionEntity) nodEntity));
            }
            if (nodEntity.getNodeType() == NodeType.Appliance) {
                childrenDto.add(new ApplianceDto((ApplianceEntity) nodEntity));
            }
        }
        this.children = childrenDto;
    }


}
