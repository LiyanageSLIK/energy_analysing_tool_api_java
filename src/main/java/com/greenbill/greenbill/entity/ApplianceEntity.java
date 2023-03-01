package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.enumerat.ApplianceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appliance")
public class ApplianceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "parent_nod_id", insertable = false, updatable = false, nullable = true)
    private String parentNodId;

    @Column(name = "node_id", nullable = false)
    private String nodeId;

    @Column(name = "appliance_name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "appliance_type", nullable = false)
    private ApplianceType applianceType;

    @Column(name = "wattage", nullable = false)
    private Integer wattage;

    @Column(name = "usage_per_day_h", nullable = false)
    private Integer usagePerDayH;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "last_updated", nullable = false)
    private Date lastUpdated= new Date();

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private SectionEntity section;

    public void setLastUpdated() {
        this.lastUpdated = new Date();
    }


}
