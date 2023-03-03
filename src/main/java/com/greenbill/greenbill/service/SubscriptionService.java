package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.SubsPlanResDto;
import com.greenbill.greenbill.entity.SubscriptionEntity;
import com.greenbill.greenbill.entity.SubscriptionPlanEntity;
import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.enumeration.PlanType;
import com.greenbill.greenbill.enumeration.Status;
import com.greenbill.greenbill.enumeration.SubscriptionPlan;
import com.greenbill.greenbill.repository.SubscriptionPlanRepository;
import com.greenbill.greenbill.repository.SubscriptionRepository;
import com.greenbill.greenbill.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    @Autowired
    private UserRepository userRepository;

    public List<SubsPlanResDto> getActiveSubsPlanList() throws HttpClientErrorException {
        List<SubsPlanResDto> activeSubsPlanList = new ArrayList<>();
        List<SubscriptionPlanEntity> listOfPlan = subscriptionPlanRepository.findByStatusAndPlanTypeNotOrderByRateAsc(Status.ACTIVE, PlanType.ADMIN);
        for (SubscriptionPlanEntity subscriptionPlan : listOfPlan) {
            activeSubsPlanList.add(new SubsPlanResDto(subscriptionPlan));
        }
        return activeSubsPlanList;
    }


    public SubscriptionPlanEntity getPlanEntityByPlanName(SubscriptionPlan planName) throws HttpClientErrorException {
        return subscriptionPlanRepository.findByName(planName);
    }

    @Transactional
    public SubscriptionEntity addSubscription(String email, SubscriptionPlan planName) throws HttpClientErrorException {
        UserEntity user = userRepository.findByEmail(email);
        SubscriptionPlanEntity subscriptionPlan = subscriptionPlanRepository.findByName(planName);
        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Email:User not found");
        }
        if (subscriptionPlan == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong planName: not found");
        }
        if (planName.equals(SubscriptionPlan.FREE)) {
            SubscriptionEntity currentSubscription = subscriptionRepository.findFirstByUser_EmailAndStatus(email, Status.ACTIVE);
            currentSubscription.setStatus(Status.INACTIVE);
            SubscriptionEntity freeSubscription = subscriptionRepository.findFirstByUser_EmailAndSubscriptionPlan_Name(email, planName);
            freeSubscription.setStatus(Status.ACTIVE);
            subscriptionRepository.save(currentSubscription);
            return subscriptionRepository.save(freeSubscription);
        } else {
            SubscriptionEntity newSubscription = new SubscriptionEntity();
            newSubscription.setSubscriptionPlan(subscriptionPlan);
            newSubscription.setUser(user);
            return subscriptionRepository.save(newSubscription);
        }
    }


}
