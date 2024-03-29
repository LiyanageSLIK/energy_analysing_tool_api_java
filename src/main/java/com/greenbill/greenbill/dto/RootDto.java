package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.entity.NodeEntity;
import com.greenbill.greenbill.entity.RootEntity;
import com.greenbill.greenbill.entity.SectionEntity;
import com.greenbill.greenbill.enumeration.NodeType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RootDto implements BaseDto {
    //    private long id;
    private String frontEndId;
    private NodeType nodeType;
    private String name;
    private List<SectionDto> children;

    public RootDto(RootEntity rootEntity) {
//        setId(rootEntity.getId());
        setName(rootEntity.getName());
        setNodeType(rootEntity.getNodeType());
        setFrontEndId(rootEntity.getFrontEndId());
        setChildren(rootEntity.getChildren());
    }

    public void setChildren(List<NodeEntity> children) {
        List<SectionDto> childrenDto = new ArrayList<>();
        if (children != null) {
            for (var nodEntity : children) {
                childrenDto.add(new SectionDto((SectionEntity) nodEntity));
            }
        }
        this.children = childrenDto;
    }
}
