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



    @Transactional
    public SubscriptionEntity addSubscription(String email, SubscriptionDto subscriptionDto) throws HttpClientErrorException {
        UserEntity requestingUser = userRepository.findByEmail(email);
        UserEntity user = userRepository.findByEmail(email);
        SubscriptionPlanName planName = subscriptionDto.getSubscriptionPlanName();
        SubscriptionPlanEntity subscriptionPlan = subscriptionPlanRepository.findByNameAndStatus(planName,Status.ACTIVE);
        if (user.getRole().equals(Role.ADMIN) ) {
            user = userRepository.findByEmail(subscriptionDto.getUserEmail());
        }
        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong Email:User not found");
        }
        if (subscriptionPlan == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wrong planName: not found");
        }
        SubscriptionEntity currentSubscription = subscriptionRepository.findFirstByUser_EmailAndStatus(user.getEmail(), Status.ACTIVE);
        if (currentSubscription == null) {
            SubscriptionPlanEntity initialPlan = subscriptionPlanRepository.findByNameAndStatus(SubscriptionPlanName.FREE,Status.ACTIVE);
            SubscriptionEntity initialSubscription = new SubscriptionEntity();
            initialSubscription.setSubscriptionPlan(initialPlan);
            initialSubscription.setUser(user);
            currentSubscription = subscriptionRepository.save(initialSubscription);
        }
        SubscriptionPlanEntity currentSubscriptionPlan = currentSubscription.getSubscriptionPlan();
        if (planName.equals(SubscriptionPlanName.FREE)) {
            if (currentSubscriptionPlan.getName().equals(SubscriptionPlanName.FREE) ) {
                return currentSubscription;
            }
//            currentSubscription.setStatus(Status.INACTIVE);
//            SubscriptionEntity freeSubscription = subscriptionRepository.findFirstByUser_EmailAndSubscriptionPlan_Name(email, planName);
//            freeSubscription.setStatus(Status.ACTIVE);
//            subscriptionRepository.save(currentSubscription);
            currentSubscription.setSubscriptionPlan(subscriptionPlan);
            return subscriptionRepository.save(currentSubscription);
        } else {
            if (!requestingUser.getRole().equals(Role.ADMIN)) {
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Only Admin can change this");
            }
//            currentSubscription.setStatus(Status.INACTIVE);
//            SubscriptionEntity newSubscription = new SubscriptionEntity();
//            newSubscription.setSubscriptionPlan(subscriptionPlan);
//            newSubscription.setUser(user);
            currentSubscription.setSubscriptionPlan(subscriptionPlan);

            return subscriptionRepository.save(currentSubscription);
        }
    }

    @Transactional
    public ActiveSubscriptionDto getActiveSubscriptionOfUser(String userEmail) {
        SubscriptionEntity currentSubscription = subscriptionRepository.findFirstByUser_EmailAndStatus(userEmail, Status.ACTIVE);
        return new ActiveSubscriptionDto(currentSubscription);
    }

    @Transactional
    public void AddNewSubscriptionPlanList (List<SubscriptionPlanEntity> subscriptionPlanList){
        for (SubscriptionPlanEntity subscriptionPlan:subscriptionPlanList) {
            SubscriptionPlanEntity currentPlan =subscriptionPlanRepository.findByName(subscriptionPlan.getName());
            if(currentPlan==null||subscriptionPlan==null){continue;}
            currentPlan.update(subscriptionPlan);
            subscriptionPlanRepository.save(currentPlan);
        }
//        List<SubscriptionPlanEntity> currentActivePlanList=subscriptionPlanRepository.findByStatus(Status.ACTIVE);
//        subscriptionPlanRepository.updateStatusByStatus(Status.INACTIVE,Status.ACTIVE);
//        subscriptionPlanRepository.saveAll(subscriptionPlanList);
    }


}
