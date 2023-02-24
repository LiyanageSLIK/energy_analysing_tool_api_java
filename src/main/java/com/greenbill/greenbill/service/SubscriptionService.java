package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.SubsPlanResDto;
import com.greenbill.greenbill.dto.UserLoginResDto;
import com.greenbill.greenbill.dto.UserRegisterDto;
import com.greenbill.greenbill.entity.SubscriptionPlanEntity;
import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.enumerat.PlanType;
import com.greenbill.greenbill.enumerat.Status;
import com.greenbill.greenbill.repository.SubscriptionPlanRepository;
import com.greenbill.greenbill.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    public List<SubsPlanResDto> getActiveSubsPlanList() throws HttpClientErrorException {
        List<SubsPlanResDto> activeSubsPlanList=new ArrayList<>();
        List<SubscriptionPlanEntity> listOfPlan=subscriptionPlanRepository.findByStatusAndPlanTypeNotOrderByRateAsc(Status.ACTIVE,PlanType.ADMIN);
        for (SubscriptionPlanEntity subscriptionPlan:listOfPlan) {
            activeSubsPlanList.add(new SubsPlanResDto(subscriptionPlan));
        }
        return activeSubsPlanList;
    }




}
