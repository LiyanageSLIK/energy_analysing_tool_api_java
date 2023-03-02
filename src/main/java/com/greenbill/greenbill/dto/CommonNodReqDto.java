package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.enumerat.ApplianceType;
import com.greenbill.greenbill.enumerat.NodType;
import com.greenbill.greenbill.enumerat.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonNodReqDto extends BaseDto {

    @NotNull
    private NodType nodType;
    @NotNull
    private String nodeId;
    @NotNull
    private String parentNodId;
    @NotNull
    private Long projectId;
    @NotNull
    private String name;
    private Status status = Status.ACTIVE;
    private ApplianceType applianceType;
    private Integer wattage;
    private Integer usagePerDayH;
    private Integer quantity;

}
