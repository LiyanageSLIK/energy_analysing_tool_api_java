package com.greenbill.greenbill.dto.response;

import com.greenbill.greenbill.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Deprecated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NestedPieChartDto implements BaseDto {
    private String name;
    private double totalUnits;
    private double unitPercentageOfParent;
    private double unitPercentageOfProject;

    public NestedPieChartDto(NodeEnergyConsumptionDetailsDto nodeEnergyConsumptionDetailsDto) {
        setName(nodeEnergyConsumptionDetailsDto.getName());
        setTotalUnits(nodeEnergyConsumptionDetailsDto.getTotalUnits());
        setUnitPercentageOfParent(nodeEnergyConsumptionDetailsDto.getUnitPercentageOfParent());
        setUnitPercentageOfProject(nodeEnergyConsumptionDetailsDto.getUnitPercentageOfProject());
    }
}
