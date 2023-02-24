package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.SubscriptionPlanEntity;
import com.greenbill.greenbill.enumerat.PlanType;
import com.greenbill.greenbill.enumerat.Status;
import com.greenbill.greenbill.enumerat.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlanEntity, Long> {
    SubscriptionPlanEntity findByName(SubscriptionPlan name);

    List<SubscriptionPlanEntity> findByStatusAndPlanTypeNotOrderByRateAsc(Status status, PlanType planType);


}