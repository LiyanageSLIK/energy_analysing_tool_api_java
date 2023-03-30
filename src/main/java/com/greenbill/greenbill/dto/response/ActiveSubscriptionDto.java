package com.greenbill.greenbill.dto.response;

import com.greenbill.greenbill.dto.BaseDto;
import com.greenbill.greenbill.entity.SubscriptionEntity;
import com.greenbill.greenbill.enumeration.SubscriptionPlanName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
public class ActiveSubscriptionDto implements BaseDto {
    private SubscriptionPlanName name;
    private long remainingDays;
    private double totalDays;

    public ActiveSubscriptionDto(SubscriptionEntity subscriptionEntity) {
        setName(subscriptionEntity.getSubscriptionPlan().getName());
        setRemainingDays(ChronoUnit.DAYS.between(LocalDate.now(), subscriptionEntity.getEndDate()));
        setTotalDays((ChronoUnit.DAYS.between(subscriptionEntity.getStartDate(), subscriptionEntity.getEndDate())));
    }
}
