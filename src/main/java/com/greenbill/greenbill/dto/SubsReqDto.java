package com.greenbill.greenbill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubsReqDto extends BaseDto {

    private String userEmail;

    private String subscriptionPlanName;

}
