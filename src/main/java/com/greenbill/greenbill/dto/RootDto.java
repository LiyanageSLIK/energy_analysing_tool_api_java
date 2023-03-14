package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.entity.NodeEntity;
import com.greenbill.greenbill.entity.RootEntity;
import com.greenbill.greenbill.entity.SectionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RootDto extends BaseDto {
    private long id;
    private List<SectionDto> children;

    public RootDto(RootEntity rootEntity) {
        setId(rootEntity.getId());
        setChildren(rootEntity.getChildren());
    }

    public void setChildren(List<NodeEntity> children) {
        List<SectionDto> childrenDto = new ArrayList<>();
        if(children!=null) {
            for (var nodEntity : children) {
                childrenDto.add(new SectionDto((SectionEntity) nodEntity));
            }
        }
        this.children = childrenDto;
    }
}
