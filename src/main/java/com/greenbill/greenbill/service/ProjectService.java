package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.ProjectDto;
import com.greenbill.greenbill.entity.*;
import com.greenbill.greenbill.enumeration.Status;
import com.greenbill.greenbill.repository.ProjectRepository;
import com.greenbill.greenbill.repository.RootRepository;
import com.greenbill.greenbill.repository.SubscriptionPlanRepository;
import com.greenbill.greenbill.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private RootRepository rootRepository;


    @Transactional
    public ProjectDto addProject(ProjectDto projectDto, String userEmail) throws Exception {
        if (!validatePlayGroundProjectAccess(userEmail)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Sorry You had reach your subscription limitations upgrade your plan for more benefits");
        }
        ProjectEntity project = new ProjectEntity(projectDto);
        SubscriptionEntity activeSubscription=subscriptionRepository.findFirstByUser_EmailAndStatus(userEmail, Status.ACTIVE);
        project.setSubscription(activeSubscription);
        RootEntity root =new RootEntity();
        root.setProject(project);
        project.setRoot(root);
        ProjectEntity test=projectRepository.save(project);
        return new ProjectDto(test);
    }
    @Transactional
    public List<ProjectDto> getAllProject(String userEmail) throws Exception {
        List<ProjectEntity> projecList = projectRepository.getBySubscription_StatusAndSubscription_User_EmailOrderByLastUpdatedDesc(Status.ACTIVE,userEmail);
        List<ProjectDto> dtoList = new ArrayList<>();
        for (ProjectEntity project : projecList) {
            dtoList.add(new ProjectDto(project));
        }
        return dtoList;
    }

    @Transactional
    public ProjectDto updateProject(ProjectDto projectDto) throws Exception {
        var project = projectRepository.getFirstById(projectDto.getProjectId());
        project.setName(projectDto.getName());
        project.setProjectType(projectDto.getProjectType());
        return new ProjectDto(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(long projectId) {
        projectRepository.removeById(projectId);
    }

    @Transactional
    public boolean validatePlayGroundProjectAccess(String email) {
        SubscriptionPlanEntity activePlan = subscriptionPlanRepository.getBySubscriptions_User_Email(email);
        int maxProjectAllow = activePlan.getMaxNumProject();
        int currentProjectCount = (int) projectRepository.countBySubscription_User_Email(email);
        return (currentProjectCount < maxProjectAllow);
    }
}
