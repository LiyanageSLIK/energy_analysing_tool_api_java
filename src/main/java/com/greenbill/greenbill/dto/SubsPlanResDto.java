package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.entity.SubscriptionPlanEntity;
import com.greenbill.greenbill.enumerat.Cycle;
import com.greenbill.greenbill.enumerat.PlanType;
import com.greenbill.greenbill.enumerat.SubscriptionPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubsPlanResDto extends BaseDto {

    private SubscriptionPlan name;
    private Float rate;
    private Cycle cycle;
    private PlanType planType;
    private Integer maxNumProject;
    private Integer maxNumNode;

    public SubsPlanResDto(SubscriptionPlanEntity subscriptionPlanEntity) {

        this.name = subscriptionPlanEntity.getName();
        this.rate = subscriptionPlanEntity.getRate();
        this.planType = subscriptionPlanEntity.getPlanType();
        this.cycle = subscriptionPlanEntity.getCycle();
        this.maxNumProject = subscriptionPlanEntity.getMaxNumProject();
        this.maxNumNode = subscriptionPlanEntity.getMaxNumNode();
    }
}
