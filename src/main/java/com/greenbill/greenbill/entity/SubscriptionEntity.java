package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.enumerat.Cycle;
import com.greenbill.greenbill.enumerat.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "subscription")
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserEntity user;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private SubscriptionPlanEntity subscriptionPlan;

    public SubscriptionEntity() {
        this.startDate = LocalDate.now();
        this.status = Status.ACTIVE;

    }

    public void setSubscriptionPlan(SubscriptionPlanEntity subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
        Cycle cycle = subscriptionPlan.getCycle();
        this.endDate = this.startDate.plusMonths(cycle.getMonth());

    }
}
