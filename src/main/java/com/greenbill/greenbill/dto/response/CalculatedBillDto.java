package com.greenbill.greenbill.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenbill.greenbill.dto.BaseDto;
import com.greenbill.greenbill.enumeration.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
public class CalculatedBillDto implements BaseDto {
    private double totalUnits;
    private String usageCharge;
    private String fixedCharge;
    private String totalCharge;
    private String levy;
    private String billAmount;
    private List<Object> calculationSteps;
    @JsonIgnore
    private NumberFormat format;

    public CalculatedBillDto(CurrencyCode currencyCode) {
        Currency currency = Currency.getInstance(String.valueOf(currencyCode));
        this. format = NumberFormat.getCurrencyInstance(new Locale("en", "LK"));
        format.setCurrency(currency);
    }

    public void setTotalUnits(double totalUnits) {
        this.totalUnits = totalUnits;
    }

    public void setUsageCharge(double usageCharge) {
        this.usageCharge = format.format(usageCharge).replace(format.getCurrency().getSymbol(), format.getCurrency().getSymbol() + " ");
    }

    public void setFixedCharge(double fixedCharge) {
        this.fixedCharge = format.format(fixedCharge).replace(format.getCurrency().getSymbol(), format.getCurrency().getSymbol() + " ");
    }

    public void setTotalCharge(double totalCharge) {
        this.totalCharge = format.format(totalCharge).replace(format.getCurrency().getSymbol(), format.getCurrency().getSymbol() + " ");
    }

    public void setLevy(double levy) {
        this.levy = format.format(levy).replace(format.getCurrency().getSymbol(), format.getCurrency().getSymbol() + " ");
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = format.format(billAmount).replace(format.getCurrency().getSymbol(), format.getCurrency().getSymbol() + " ");
    }

    public void setCalculationSteps(List<Object> calculationSteps) {
        this.calculationSteps = calculationSteps;
    }
}
