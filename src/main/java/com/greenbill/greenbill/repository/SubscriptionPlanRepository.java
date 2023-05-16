package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.SubscriptionPlanEntity;
import com.greenbill.greenbill.enumeration.PlanType;
import com.greenbill.greenbill.enumeration.Status;
import com.greenbill.greenbill.enumeration.SubscriptionPlanName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlanEntity, Long> {
    SubscriptionPlanEntity findByNameAndStatus(SubscriptionPlanName name, Status status);
    @Transactional
    @Modifying
    @Query("update SubscriptionPlanEntity s set s.status = ?1 where s.status = ?2")
    void updateStatusByStatus(Status status, Status status1);
    List<SubscriptionPlanEntity> findByStatus(Status status);

    SubscriptionPlanEntity getBySubscriptions_User_Email(String email);


    List<SubscriptionPlanEntity> findByStatusAndPlanTypeNotOrderByRateAsc(Status status, PlanType planType);


}