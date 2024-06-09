package com.greenbill.greenbill.dto.response;

import com.greenbill.greenbill.dto.BaseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GraphNodeDto implements BaseDto {
    private String frontEndId;
    private String name;
    private double totalUnits;
    private List<GraphNodeDto> children;
    private String parentName;

    public GraphNodeDto(String frontEndId, String name, double totalUnits, List<GraphNodeDto> children) {
        this.frontEndId = frontEndId;
        this.name = name;
        this.totalUnits = totalUnits;
        this.children = children;
    }
}
