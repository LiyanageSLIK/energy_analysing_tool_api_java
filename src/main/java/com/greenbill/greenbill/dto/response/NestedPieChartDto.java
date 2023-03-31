package com.greenbill.greenbill.dto.response;

import com.greenbill.greenbill.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NestedPieChartDto implements BaseDto {
    private String name;
    private double totalUnits;
    private double unitPercentageOfParent;

    public NestedPieChartDto(NodeEnergyConsumptionDetailsDto nodeEnergyConsumptionDetailsDto) {
        setName(nodeEnergyConsumptionDetailsDto.getName());
        setTotalUnits(nodeEnergyConsumptionDetailsDto.getTotalUnits());
        setUnitPercentageOfParent(nodeEnergyConsumptionDetailsDto.getUnitPercentageOfParent());
    }
}
