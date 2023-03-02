package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.enumerat.ApplianceType;
import com.greenbill.greenbill.enumerat.NodType;
import com.greenbill.greenbill.enumerat.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonNodReqDto extends BaseDto{

    private NodType nodType;
    private String nodeId;
    private String parentNodId;
    private Long projectId;
    private String name;
    private Status status;
    private ApplianceType applianceType;
    private Integer wattage;
    private Integer usagePerDayH;
    private Integer quantity;

}
