package com.greenbill.greenbill.dto.request;


import com.greenbill.greenbill.dto.BaseDto;
import com.greenbill.greenbill.enumeration.ApplianceType;
import com.greenbill.greenbill.enumeration.NodeType;
import lombok.Data;

@Data
public class NodeRequestDto extends BaseDto {

    private String frontEndId;
    private String parentFrontEndId;
    private String name;
    private NodeType nodeType;
    private double wattRate;
    private double hours;
    private ApplianceType applianceType;
    private int quantity;
}
