package com.greenbill.greenbill.dto.response;

import com.greenbill.greenbill.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminDashboardDto implements BaseDto {

    private long totalUsers;
    private long totalProjects;



}
