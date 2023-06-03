package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.ProjectDto;
import com.greenbill.greenbill.dto.response.ProjectSummaryDto;
import com.greenbill.greenbill.entity.ProjectEntity;
import com.greenbill.greenbill.entity.RootEntity;
import com.greenbill.greenbill.entity.SubscriptionEntity;
import com.greenbill.greenbill.entity.SubscriptionPlanEntity;
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
    public ProjectSummaryDto addProject(ProjectDto projectDto, String userEmail) throws Exception {
        if (!validatePlayGroundProjectAccess(userEmail)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Sorry You had reach your subscription limitations upgrade your plan for more benefits");
        }
        ProjectEntity project = new ProjectEntity(projectDto);
        SubscriptionEntity activeSubscription = subscriptionRepository.findFirstByUser_EmailAndStatus(userEmail, Status.ACTIVE);
        project.setSubscription(activeSubscription);
        RootEntity root = new RootEntity();
        root.setName(project.getName());
        root.setProject(project);
        project.setRoot(root);
        return new ProjectSummaryDto(projectRepository.save(project));
    }

    @Transactional
    public List<ProjectSummaryDto> getAllProject(String userEmail) throws Exception {
        List<ProjectEntity> projecList = projectRepository.getBySubscription_StatusAndSubscription_User_EmailOrderByLastUpdatedDesc(Status.ACTIVE, userEmail);
        List<ProjectSummaryDto> dtoList = new ArrayList<>();
        for (ProjectEntity project : projecList) {
            dtoList.add(new ProjectSummaryDto(project));
        }
        return dtoList;
    }

    @Transactional
    public ProjectDto getProject(long projectId) throws Exception {
        var project = projectRepository.getById(projectId);
        return new ProjectDto(project);
    }

    @Transactional
    public ProjectSummaryDto updateProject(ProjectDto projectDto) throws Exception {
        var project = projectRepository.getFirstById(projectDto.getProjectId());
        project.setName(projectDto.getName());
        project.setProjectType(projectDto.getProjectType());
        return new ProjectSummaryDto(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(long projectId) {
        projectRepository.removeById(projectId);
    }

    @Transactional
    public boolean validatePlayGroundProjectAccess(String email) {
        SubscriptionPlanEntity activePlan = subscriptionPlanRepository.findBySubscriptions_StatusAndSubscriptions_User_Email(Status.ACTIVE,email);
        if (activePlan == null) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Sorry You had no subscribe any subscription plan yet");
        }
        int maxProjectAllow = activePlan.getMaxNumProject();
        int currentProjectCount = (int) projectRepository.countBySubscription_User_Email(email);
        return (currentProjectCount < maxProjectAllow);
    }
}
