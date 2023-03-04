package com.greenbill.greenbill.entity.refactor;

import com.greenbill.greenbill.enumeration.ApplianceType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "appliance")
@Getter
@Setter
@NoArgsConstructor
public class ApplianceEntity extends NodeEntity {

    @Column(name = "watt_rate", nullable = false)
    private Double wattRate;

    @Column(name = "hours", nullable = false)
    private Double hours;

    @Column(name = "appliance_type", nullable = false)
    private ApplianceType applianceType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private NodeEntity parent;
}
