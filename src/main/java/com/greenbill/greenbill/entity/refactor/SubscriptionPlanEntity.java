package com.greenbill.greenbill.entity.refactor;

import com.greenbill.greenbill.enumeration.*;
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

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private SubscriptionPlanName name;

    @Column(nullable = false)
    private Float rate;

    @Column(name = "currency_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Cycle cycle;

    @Column(name = "plan_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @Column(name = "max_num_project", nullable = true)
    private Integer maxNumProject;

    @Column(name = "max_num_node", nullable = true)
    private Integer maxNumNode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "subscriptionPlan", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<SubscriptionEntity> subscriptions = new ArrayList<>();

}

