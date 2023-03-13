package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.entity.NodeEntity;
import com.greenbill.greenbill.entity.RootEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RootDto extends BaseDto {
    private long id;
    private List<NodeEntity> children;

    public RootDto(RootEntity rootEntity) {
        setId(rootEntity.getId());
        setChildren(rootEntity.getChildren());
    }
}
