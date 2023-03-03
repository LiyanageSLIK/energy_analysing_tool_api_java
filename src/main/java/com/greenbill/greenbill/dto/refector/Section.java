package com.greenbill.greenbill.dto.refector;

import com.greenbill.greenbill.dto.refector.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Section extends Node {
    private List<Node> children;
}
