package com.greenbill.greenbill.dto.refactor;

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
}
