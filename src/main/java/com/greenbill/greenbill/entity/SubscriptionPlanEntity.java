package com.greenbill.greenbill.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscription_plan")
public class SubscriptionPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer rate;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String cycle;

    @Column(nullable = false)
    private String type;

    @Column(name = "max_num_project", nullable = false)
    private Integer maxNumProject;

    @Column(nullable = false)
    private Boolean status;

    @OneToMany(mappedBy = "subscriptionPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubscriptionEntity> subscriptions = new ArrayList<>();

}

