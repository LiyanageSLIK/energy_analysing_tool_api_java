package com.greenbill.greenbill.entity.refactor;

import com.greenbill.greenbill.enumeration.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
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

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectEntity> projects = new ArrayList<>();

//    public SubscriptionEntity() {
//        this.startDate = LocalDate.now();
//        this.status = Status.ACTIVE;
//    }
//
//    public void setSubscriptionPlan(SubscriptionPlanEntity subscriptionPlan) {
//        this.subscriptionPlan = subscriptionPlan;
//        Cycle cycle = subscriptionPlan.getCycle();
//        this.endDate = this.startDate.plusMonths(cycle.getMonth());
//    }
}
