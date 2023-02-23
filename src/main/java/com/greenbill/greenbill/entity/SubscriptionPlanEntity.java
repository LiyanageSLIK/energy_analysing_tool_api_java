package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.enumerat.CurrencyCode;
import com.greenbill.greenbill.enumerat.PlanType;
import com.greenbill.greenbill.enumerat.Status;
import com.greenbill.greenbill.enumerat.SubscriptionPlan;
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
    @Enumerated(EnumType.STRING)
    private SubscriptionPlan name;

    @Column(nullable = false)
    private Integer rate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode;

    @Column(nullable = false)
    private String cycle;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @Column(name = "max_num_project", nullable = false)
    private Integer maxNumProject;

    @Column(name = "max_num_node", nullable = false)
    private Integer maxNumNode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "subscriptionPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubscriptionEntity> subscriptions = new ArrayList<>();

}

