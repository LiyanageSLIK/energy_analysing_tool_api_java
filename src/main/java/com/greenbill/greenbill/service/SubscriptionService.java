package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.request.SubscriptionDto;
import com.greenbill.greenbill.dto.response.ActiveSubscriptionDto;
import com.greenbill.greenbill.dto.response.SubscriptionPlanResponseDto;
import com.greenbill.greenbill.entity.SubscriptionEntity;
import com.greenbill.greenbill.entity.SubscriptionPlanEntity;
import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.enumeration.PlanType;
import com.greenbill.greenbill.enumeration.Role;
import com.greenbill.greenbill.enumeration.Status;
import com.greenbill.greenbill.enumeration.SubscriptionPlanName;
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

    public List<SubscriptionPlanResponseDto> getActiveSubsPlanList() throws HttpClientErrorException {
        List<SubscriptionPlanResponseDto> activeSubsPlanList = new ArrayList<>();
        List<SubscriptionPlanEntity> listOfPlan = subscriptionPlanRepository.findByStatusAndPlanTypeNotOrderByRateAsc(Status.ACTIVE, PlanType.ADMIN);
        for (SubscriptionPlanEntity subscriptionPlan : listOfPlan) {
            activeSubsPlanList.add(new SubscriptionPlanResponseDto(subscriptionPlan));
        }
        return activeSubsPlanList;
    }


    public SubscriptionPlanEntity getPlanEntityByPlanName(SubscriptionPlanName planName) throws HttpClientErrorException {
        return subscriptionPlanRepository.findByName(planName);
    }

    @Transactional
    public SubscriptionEntity addSubscription(String email, SubscriptionDto subscriptionDto) throws HttpClientErrorException {
        UserEntity user = userRepository.findByEmail(email);
        SubscriptionPlanName planName = subscriptionDto.getSubscriptionPlanName();
        SubscriptionPlanEntity subscriptionPlan = subscriptionPlanRepository.findByName(planName);
        if (user.getRole() == Role.ADMIN) {
            user = userRepository.findByEmail(subscriptionDto.getUserEmail());
        }
        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Email:User not found");
        }
        if (subscriptionPlan == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong planName: not found");
        }
        SubscriptionEntity currentSubscription = subscriptionRepository.findFirstByUser_EmailAndStatus(email, Status.ACTIVE);
        if (currentSubscription == null) {
            SubscriptionPlanEntity initialPlan = subscriptionPlanRepository.findByName(SubscriptionPlanName.FREE);
            SubscriptionEntity initialSubscription = new SubscriptionEntity();
            initialSubscription.setSubscriptionPlan(initialPlan);
            initialSubscription.setUser(userRepository.save(user));
            currentSubscription = subscriptionRepository.save(initialSubscription);
        }
        SubscriptionPlanEntity currentSubscriptionPlan = currentSubscription.getSubscriptionPlan();
        if (planName.equals(SubscriptionPlanName.FREE)) {
            if (currentSubscriptionPlan.getName() == SubscriptionPlanName.FREE) {
                return currentSubscription;
            }
            currentSubscription.setStatus(Status.INACTIVE);
            SubscriptionEntity freeSubscription = subscriptionRepository.findFirstByUser_EmailAndSubscriptionPlan_Name(email, planName);
            freeSubscription.setStatus(Status.ACTIVE);
            subscriptionRepository.save(currentSubscription);
            return subscriptionRepository.save(freeSubscription);
        } else {
            if (user.getRole() != Role.ADMIN) {
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Only Admin can change this");
            }
            currentSubscription.setStatus(Status.INACTIVE);
            SubscriptionEntity newSubscription = new SubscriptionEntity();
            newSubscription.setSubscriptionPlan(subscriptionPlan);
            newSubscription.setUser(user);
            return subscriptionRepository.save(newSubscription);
        }
    }

    @Transactional
    public ActiveSubscriptionDto getActiveSubscriptionOfUser(String userEmail) {
        SubscriptionEntity currentSubscription = subscriptionRepository.findFirstByUser_EmailAndStatus(userEmail, Status.ACTIVE);
        return new ActiveSubscriptionDto(currentSubscription);
    }

    @Transactional
    public void AddNewSubscriptionPlanList (List<SubscriptionPlanEntity> subscriptionPlanList){
        List<SubscriptionPlanEntity> currentActivePlanList=subscriptionPlanRepository.findByStatus(Status.ACTIVE);
        subscriptionPlanRepository.updateStatusByStatus(Status.INACTIVE,Status.ACTIVE);
        subscriptionPlanRepository.saveAll(subscriptionPlanList);
    }


}
