package com.greenbill.greenbill.dto.request;

import com.greenbill.greenbill.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto extends BaseDto {

    private String userEmail;

    private String subscriptionPlanName;

}
