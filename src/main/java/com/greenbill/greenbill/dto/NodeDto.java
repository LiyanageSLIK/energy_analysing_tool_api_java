package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.entity.NodeEntity;
import com.greenbill.greenbill.enumeration.NodeType;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public  class NodeDto extends BaseDto {

    private long id;
    private String frontEndId;
    private String name;
    private NodeType nodeType;


}
