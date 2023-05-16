package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.enumeration.CurrencyCode;
import com.greenbill.greenbill.enumeration.ProjectType;
import com.greenbill.greenbill.enumeration.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tariff")
public class TariffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", nullable = false)
    private String batchId;

    @Column(name = "effective_from", nullable = false)
    private Date effectiveFrom;

    @Column(nullable = false)
    private String country;

    @Column(name = "currency_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectType category;

    @Column(name = "limited_from", nullable = false)
    private double limitedFrom;

    @Column(name = "limited_to", nullable = false)
    private double limitedTo;

    @Column(name = "lower_limit", nullable = false)
    private double lowerLimit;

    @Column(name = "upper_limit", nullable = false)
    private double upperLimit;

    @Column(name = "energy_charge", nullable = false)
    private double energyCharge;

    @Column(name = "fixed_charge", nullable = false)
    private double fixedCharge;

    @Column(name = "levy", nullable = false)
    private double Levy;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;


    public void setLimitedTo(double limitedTo) {
        if(limitedTo==-1){
            this.limitedTo = Integer.MAX_VALUE;
        }else {
            this.limitedTo = limitedTo;
        }
    }


    public void setUpperLimit(double upperLimit) {
        if(upperLimit==-1){
            this.upperLimit = Integer.MAX_VALUE;
        }else {
            this.upperLimit = upperLimit;
        }
    }

    public void setEffectiveFrom(String effectiveFrom) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.effectiveFrom = dateFormat.parse(effectiveFrom);
    }
}
