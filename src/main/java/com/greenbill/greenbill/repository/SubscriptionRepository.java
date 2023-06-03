package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.SubscriptionEntity;
import com.greenbill.greenbill.enumeration.PlanType;
import com.greenbill.greenbill.enumeration.Status;
import com.greenbill.greenbill.enumeration.SubscriptionPlanName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    long countBySubscriptionPlan_PlanType(PlanType planType);
    SubscriptionEntity findFirstByUser_EmailAndStatus(String email, Status status);

    SubscriptionEntity findFirstByUser_EmailAndSubscriptionPlan_Name(String email, SubscriptionPlanName name);
}