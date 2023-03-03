package com.greenbill.greenbill.dto.refector;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenbill.greenbill.enumeration.NodeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class Node {
    private String id;
    private String parentId;
    private String name;
    private NodeType nodeType;
}
