package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.response.AdminDashboardDto;
import com.greenbill.greenbill.enumeration.PlanType;
import com.greenbill.greenbill.repository.ProjectRepository;
import com.greenbill.greenbill.repository.SubscriptionRepository;
import com.greenbill.greenbill.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public AdminDashboardDto getStatistic(){
        long totalUsers= userRepository.count();
        long totalProject= projectRepository.count();
        long activePaidSubscription=subscriptionRepository.countBySubscriptionPlan_PlanType(PlanType.PAID);
        AdminDashboardDto result=new AdminDashboardDto(totalUsers,totalProject);
        return  result;
    }

}
