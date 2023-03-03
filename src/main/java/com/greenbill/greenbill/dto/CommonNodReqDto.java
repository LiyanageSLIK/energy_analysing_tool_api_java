package com.greenbill.greenbill.dto;

import com.greenbill.greenbill.enumeration.ApplianceType;
import com.greenbill.greenbill.enumeration.NodeType;
import com.greenbill.greenbill.enumeration.Status;
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
    private NodeType nodeType;
    @NotNull
    private String nodId;
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
