package com.greenbill.greenbill.entity;

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
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = false)
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

    public void update(SubscriptionPlanEntity newPlan){
        setName(newPlan.getName());
        setRate(newPlan.getRate());
        setCurrencyCode(newPlan.getCurrencyCode());
        setCycle(newPlan.getCycle());
        setPlanType(newPlan.getPlanType());
        setMaxNumProject(newPlan.getMaxNumProject());
        setMaxNumNode(newPlan.getMaxNumNode());
        setStatus(newPlan.getStatus());
    }

    public void setMaxNumProject(Integer maxNumProject) {
        if(maxNumProject==-1){
            this.maxNumProject = Integer.MAX_VALUE;
        }else {
            this.maxNumProject = maxNumProject;
        }
    }

    public void setMaxNumNode(Integer maxNumNode) {
        if(maxNumNode==-1){
            this.maxNumNode = Integer.MAX_VALUE;
        }else {
            this.maxNumNode = maxNumNode;
        }
    }
}

