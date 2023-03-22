package com.greenbill.greenbill.dto.response;


import com.greenbill.greenbill.dto.ApplianceDto;
import com.greenbill.greenbill.dto.NodeDto;
import com.greenbill.greenbill.dto.SectionDto;
import com.greenbill.greenbill.entity.ApplianceEntity;
import com.greenbill.greenbill.entity.NodeEntity;
import com.greenbill.greenbill.entity.SectionEntity;
import com.greenbill.greenbill.enumeration.NodeType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class SectionSummaryDto extends NodeDto {
    private List<NodeDto> children;

    public SectionSummaryDto(SectionEntity section) {
        setFrontEndId(section.getFrontEndId());
        setName(section.getName());
        setNodeType(section.getNodeType());
    }
    public void setChildren(List<NodeEntity> children) {
        List<NodeDto> childrenDto = new ArrayList<>();
        for (var nodEntity : children) {
            if (nodEntity.getNodeType() == NodeType.Section) {
                childrenDto.add(new SectionSummaryDto((SectionEntity) nodEntity));
            }
            if (nodEntity.getNodeType() == NodeType.Appliance) {
                childrenDto.add(new ApplianceDto((ApplianceEntity) nodEntity));
            }
        }
        this.children = childrenDto;
    }
}
