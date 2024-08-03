package com.greenbill.greenbill.dto.request;

import com.greenbill.greenbill.dto.BaseDto;
import lombok.Data;

@Data
public class SolarTariffRequestDto implements BaseDto {
    private Long projectId;
    private Double solarTariffRate;

}
