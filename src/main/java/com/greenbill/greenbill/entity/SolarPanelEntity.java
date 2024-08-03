package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.dto.response.SolarPanelDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "solar_panel")
public class SolarPanelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @ToString.Exclude
    private ProjectEntity project;

    @Column(name = "watt_rate", nullable = false)
    private Double wattRate;

    @Column(name = "hours", nullable = false)
    private Double hours;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public SolarPanelEntity(SolarPanelDto panelDto) {
        this.wattRate = panelDto.getWattRate();
        this.hours = panelDto.getHours();
        this.quantity = panelDto.getQuantity();
    }
}
