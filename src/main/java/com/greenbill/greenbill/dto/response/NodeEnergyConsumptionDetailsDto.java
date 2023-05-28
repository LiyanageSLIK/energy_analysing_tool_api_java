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
public class NodeEnergyConsumptionDetailsDto implements BaseDto {

    private String frontEndId;
    private String name;
    private double totalUnits;
    private double unitPercentageOfParent;
    private double unitPercentageOfProject=0;
    private List<NodeEnergyConsumptionDetailsDto> children;

    public NodeEnergyConsumptionDetailsDto(ApplianceEntity appliance) {
        setName(appliance.getName());
        setFrontEndId(appliance.getFrontEndId());
        setTotalUnits(appliance.getWattRate(), appliance.getHours(),appliance.getQuantity());
    }

    public NodeEnergyConsumptionDetailsDto(NodeEntity node) {
        setFrontEndId(node.getFrontEndId());
        setName(node.getName());
    }

    public void setTotalUnits(double wattRate, double hours,Integer quantity) {
        this.totalUnits = (wattRate * hours * quantity) * 30 / 1000;
    }

    public void setUnitPercentageOfParent(double totalUnitsOfParent) {
        this.unitPercentageOfParent = (this.totalUnits / totalUnitsOfParent) * 100;
    }

    public void setUnitPercentageOfProject(double totalUnitsOfProject) {
        this.unitPercentageOfProject = (this.totalUnits / totalUnitsOfProject) * 100;
    }
}
