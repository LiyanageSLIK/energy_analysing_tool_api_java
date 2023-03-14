package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.entity.ApplianceEntity;
import com.greenbill.greenbill.enumeration.ApplianceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ApplianceDto extends NodeDto {
    private double wattRate;
    private double hours;
    private ApplianceType applianceType;
    private int quantity;

    public ApplianceDto(ApplianceEntity applianceEntity) {
        setId(applianceEntity.getId());
        setFrontEndId(applianceEntity.getFrontEndId());
        setName(applianceEntity.getName());
        setNodeType(applianceEntity.getNodeType());
        setWattRate(applianceEntity.getWattRate());
        setHours(applianceEntity.getHours());
        setApplianceType(applianceEntity.getApplianceType());
        setQuantity(applianceEntity.getQuantity());
    }
}
