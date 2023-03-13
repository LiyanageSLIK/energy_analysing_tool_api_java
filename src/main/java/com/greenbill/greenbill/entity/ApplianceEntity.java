package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.dto.ApplianceDto;
import com.greenbill.greenbill.enumeration.ApplianceType;
import com.greenbill.greenbill.enumeration.NodeType;
import com.greenbill.greenbill.enumeration.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name = "appliance")
public class ApplianceEntity extends NodeEntity {

    @Column(name = "watt_rate", nullable = false)
    private Double wattRate;

    @Column(name = "hours", nullable = false)
    private Double hours;

    @Column(name = "appliance_type", nullable = false)
    private ApplianceType applianceType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public ApplianceEntity(ApplianceDto applianceDto) {
        setName(applianceDto.getName());
        setNodeType(NodeType.APPLIANCE);
        setFrontEndId(applianceDto.getFrontEndId());
        setStatus(Status.ACTIVE);
        setApplianceType(applianceDto.getApplianceType());
        setWattRate(applianceDto.getWattRate());
        setHours(applianceDto.getHours());
        setQuantity(applianceDto.getQuantity());

    }
}
