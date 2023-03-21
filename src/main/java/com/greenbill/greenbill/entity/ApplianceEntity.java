package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.dto.request.NodeRequestDto;
import com.greenbill.greenbill.enumeration.ApplianceType;
import com.greenbill.greenbill.enumeration.NodeType;
import com.greenbill.greenbill.enumeration.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
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

    public ApplianceEntity(NodeRequestDto nodeRequestDto) {
        setName(nodeRequestDto.getName());
        setNodeType(NodeType.Appliance);
        setFrontEndId(nodeRequestDto.getFrontEndId());
        setStatus(Status.ACTIVE);
        setApplianceType(nodeRequestDto.getApplianceType());
        setWattRate(nodeRequestDto.getWattRate());
        setHours(nodeRequestDto.getHours());
        setQuantity(nodeRequestDto.getQuantity());

    }

    public void update(NodeRequestDto nodeRequestDto) {
        setName(nodeRequestDto.getName());
        setApplianceType(nodeRequestDto.getApplianceType());
        setWattRate(nodeRequestDto.getWattRate());
        setHours(nodeRequestDto.getHours());
        setQuantity(nodeRequestDto.getQuantity());
    }
}
