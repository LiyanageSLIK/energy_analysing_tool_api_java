package com.greenbill.greenbill.dto.request;

import com.greenbill.greenbill.dto.BaseDto;
import com.greenbill.greenbill.enumeration.SubscriptionPlanName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto implements BaseDto {

    private String userEmail;

    private SubscriptionPlanName subscriptionPlanName;

}
