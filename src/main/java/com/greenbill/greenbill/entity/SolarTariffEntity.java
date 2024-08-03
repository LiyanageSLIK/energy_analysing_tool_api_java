package com.greenbill.greenbill.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "solar_tariff")
public class SolarTariffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private ProjectEntity project;

    @Column(name = "solar_tariff_rate", nullable = false)
    private Double solarTariffRate;

    public SolarTariffEntity(ProjectEntity project, Double solarTariffRate) {
        this.project = project;
        this.solarTariffRate = solarTariffRate;
    }
}
