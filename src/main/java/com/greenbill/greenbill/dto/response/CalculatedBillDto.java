package com.greenbill.greenbill.dto.response;

import com.greenbill.greenbill.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculatedBillDto implements BaseDto {
    private double totalUnits;
    private double usageCharge;
    private double fixedCharge;
    private double totalCharge;
    private double levy;
    private double billAmount;
    private List<String> calculationSteps;
}
