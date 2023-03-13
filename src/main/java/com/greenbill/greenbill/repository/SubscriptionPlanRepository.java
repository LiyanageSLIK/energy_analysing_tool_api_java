package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.SubscriptionPlanEntity;
import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.enumeration.PlanType;
import com.greenbill.greenbill.enumeration.Status;
import com.greenbill.greenbill.enumeration.SubscriptionPlanName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlanEntity, Long> {
    SubscriptionPlanEntity getBySubscriptions_User(UserEntity user);

    SubscriptionPlanEntity getBySubscriptions_User_Email(String email);

    SubscriptionPlanEntity findByName(SubscriptionPlanName name);

    List<SubscriptionPlanEntity> findByStatusAndPlanTypeNotOrderByRateAsc(Status status, PlanType planType);


}