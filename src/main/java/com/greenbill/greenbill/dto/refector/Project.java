package com.greenbill.greenbill.dto.refector;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Project {
    private String id;
    private String name;
    private List<Node> children;
}
