package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.SubscriptionEntity;
import com.greenbill.greenbill.enumerat.Status;
import com.greenbill.greenbill.enumerat.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    SubscriptionEntity findFirstByUser_EmailAndStatus(String email, Status status);

    SubscriptionEntity findFirstByUser_EmailAndSubscriptionPlan_Name(String email, SubscriptionPlan name);
}