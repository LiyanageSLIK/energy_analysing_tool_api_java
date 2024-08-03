package com.greenbill.greenbill.dto.response;

import com.greenbill.greenbill.entity.SolarPanelEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SolarPanelDto {
    private Long id;
    private Double wattRate;
    private Double hours;
    private Integer quantity;
    private Long projectId;

    public SolarPanelDto(SolarPanelEntity solarPanelEntity) {
        this.id = solarPanelEntity.getId();
        this.wattRate = solarPanelEntity.getWattRate();
        this.hours = solarPanelEntity.getHours();
        this.quantity = solarPanelEntity.getQuantity();
        this.projectId = solarPanelEntity.getProject().getId();
    }
}
