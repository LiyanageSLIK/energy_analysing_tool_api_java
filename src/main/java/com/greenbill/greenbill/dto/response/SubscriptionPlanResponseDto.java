package com.greenbill.greenbill.dto.response;

import com.greenbill.greenbill.dto.BaseDto;
import com.greenbill.greenbill.entity.SubscriptionPlanEntity;
import com.greenbill.greenbill.enumeration.Cycle;
import com.greenbill.greenbill.enumeration.PlanType;
import com.greenbill.greenbill.enumeration.SubscriptionPlanName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanResponseDto extends BaseDto {

    private SubscriptionPlanName name;
    private Float rate;
    private Cycle cycle;
    private PlanType planType;
    private Integer maxNumProject;
    private Integer maxNumNode;

    public SubscriptionPlanResponseDto(SubscriptionPlanEntity subscriptionPlanEntity) {
        this.name = subscriptionPlanEntity.getName();
        this.rate = subscriptionPlanEntity.getRate();
        this.planType = subscriptionPlanEntity.getPlanType();
        this.cycle = subscriptionPlanEntity.getCycle();
        this.maxNumProject = subscriptionPlanEntity.getMaxNumProject();
        this.maxNumNode = subscriptionPlanEntity.getMaxNumNode();
    }
}
