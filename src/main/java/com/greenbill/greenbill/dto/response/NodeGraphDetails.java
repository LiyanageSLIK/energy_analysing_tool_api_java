package com.greenbill.greenbill.dto.response;

import com.greenbill.greenbill.dto.BaseDto;
import com.greenbill.greenbill.entity.ApplianceEntity;
import com.greenbill.greenbill.entity.NodeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeGraphDetails implements BaseDto {

    private String frontEndId;
    private String name;
    private double totalUnits;
    private double unitPercentageOfParent;
    private List<NodeGraphDetails> children;

    public NodeGraphDetails(ApplianceEntity appliance) {
        setName(appliance.getName());
        setFrontEndId(appliance.getFrontEndId());
        setTotalUnits(appliance.getWattRate(), appliance.getHours());
    }

    public NodeGraphDetails(NodeEntity node) {
        setFrontEndId(node.getFrontEndId());
        setName(node.getName());
    }

    public void setTotalUnits(double wattRate, double hours) {
        this.totalUnits = (wattRate * hours) * 30 / 1000;
    }

    public void setUnitPercentageOfParent(double totalUnitsOfParent) {
        this.unitPercentageOfParent = (this.totalUnits / totalUnitsOfParent) * 100;
    }
}
