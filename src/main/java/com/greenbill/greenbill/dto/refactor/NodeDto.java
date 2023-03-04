package com.greenbill.greenbill.dto.refactor;

import com.greenbill.greenbill.enumeration.NodeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class NodeDto extends BaseDto {
    private String id;
    private String parentId;
    private String name;
    private NodeType nodeType;
}
