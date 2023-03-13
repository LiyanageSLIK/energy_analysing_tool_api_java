package com.greenbill.greenbill.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenbill.greenbill.dto.NodeDto;
import com.greenbill.greenbill.dto.ProjectDto;
import com.greenbill.greenbill.dto.SectionDto;
import com.greenbill.greenbill.entity.*;
import com.greenbill.greenbill.enumeration.NodeType;
import com.greenbill.greenbill.enumeration.Status;
import com.greenbill.greenbill.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PlayGroundService {
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private RootRepository rootRepository;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private ApplianceRepository applianceRepository;
    @Autowired
    private UserService userService;


    @Transactional
    public ProjectDto addProject(ProjectDto projectDto, String userEmail) throws Exception {
        if (!validatePlayGroundProjectAccess(userEmail)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Sorry You had reach your subscription limitations upgrade your plan for more benefits");
        }
        ProjectEntity project = new ProjectEntity(projectDto);
        var activeSubscription=subscriptionRepository.findFirstByUser_EmailAndStatus(userEmail, Status.ACTIVE);
        project.setSubscription(activeSubscription);
        return new ProjectDto(projectRepository.save(project));
    }

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
    public void addNode(NodeDto nodeDto, String userEmail) throws Exception {
        if (!validatePlayGroundNodAccess(userEmail)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Sorry You had reach your subscription limitations upgrade your plan for more benefits");
        }
        NodeType nodeType = nodeDto.getNodeType();
        RootEntity root=rootRepository.findByProject_Id(nodeDto.getProjectId());
        if (nodeType == NodeType.SECTION) {
            SectionEntity savedSection = new SectionEntity();
            if (nodeDto.getParentFrontEndId().equals("root")) {
                SectionEntity sectionToSave = new SectionEntity((SectionDto) nodeDto);
                var project=root.getProject();
                project.setLastUpdated(new Date());
                root.setProject(project);
                sectionToSave.setParent(root);
                savedSection = sectionRepository.save(sectionToSave);
            } else {
                String parentFrontEndId = nodeDto.getParentFrontEndId();
                long referenceProjectId = nodeDto.getProjectId();
                SectionEntity parentSection = sectionRepository.find
                if (parentSection == null) {
                    throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Cant map Nod:Parent Nod not Found ");
                }
                SectionEntity section = new SectionEntity(commonNodReqDto);
                section.setParentSection(parentSection);
                section.setUserEmail(userEmail);
                project.setLastUpdated();
                projectRepository.save(project);
                savedSection = sectionRepository.save(section);
            }
        }
        if (nodeType == NodeType.APPLIANCE) {
            ApplianceEntity savedAppliance = new ApplianceEntity();
            String parentNodId = commonNodReqDto.getParentNodId();
            long referenceProjectId = commonNodReqDto.getProjectId();
            SectionEntity parentSection = sectionRepository.findByReferenceProjectIdAndNodId(referenceProjectId, parentNodId);
            if (parentSection == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Cant map Nod:Parent Nod not Found ");
            }
            ApplianceEntity appliance = new ApplianceEntity(commonNodReqDto);
            appliance.setSection(parentSection);
            appliance.setUserEmail(userEmail);
            project.setLastUpdated();
            projectRepository.save(project);
            savedAppliance = applianceRepository.save(appliance);
        }
    }

    @Transactional
    public void updateNod(CommonNodReqDto commonNodReqDto) throws Exception {
        NodeType nodeType = commonNodReqDto.getNodeType();
        ProjectEntity project = projectRepository.getFirstById(commonNodReqDto.getProjectId());
        if (nodeType == NodeType.SECTION) {
            String nodId = commonNodReqDto.getNodId();
            long referenceProjectId = commonNodReqDto.getProjectId();
            SectionEntity thisSection = sectionRepository.findByNodIdAndReferenceProjectId(nodId, referenceProjectId);
            if (thisSection == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "SectionDto not found");
            } else {
                thisSection.updateSection(commonNodReqDto);
                project.setLastUpdated();
                projectRepository.save(project);
                thisSection = sectionRepository.save(thisSection);
            }
        }
        if (nodeType == NodeType.APPLIANCE) {
            String nodId = commonNodReqDto.getNodId();
            long referenceProjectId = commonNodReqDto.getProjectId();
            ApplianceEntity thisAppliance = applianceRepository.findByNodIdAndReferenceProjectId(nodId, referenceProjectId);
            if (thisAppliance == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ApplianceDto not found");
            } else {
                thisAppliance.updateSection(commonNodReqDto);
                project.setLastUpdated();
                projectRepository.save(project);
                thisAppliance = applianceRepository.save(thisAppliance);
            }
        }
    }

    public void deleteNod(NodeDeleteRequestDto nodeDeleteRequestDto) {
        SectionEntity section = sectionRepository.findByReferenceProjectIdAndNodId(nodeDeleteRequestDto.getProjectId(), nodeDeleteRequestDto.getNodId());
        ApplianceEntity appliance = applianceRepository.findByReferenceProjectIdAndNodId(nodeDeleteRequestDto.getProjectId(), nodeDeleteRequestDto.getNodId());
        if (section != null) {
            sectionRepository.delete(section);
        }
        if (appliance != null) {
            applianceRepository.delete(appliance);
        }
    }


    @Transactional
    public boolean validatePlayGroundProjectAccess(String email) {
        UserEntity user = (UserEntity) userService.loadUserByUsername(email);
        SubscriptionPlanEntity activePlan = subscriptionPlanRepository.getBySubscriptions_User(user);
        int maxProjectAllow = activePlan.getMaxNumProject();
        int currentProjectCount = (int) projectRepository.countByUser(user);
        return (currentProjectCount < maxProjectAllow);
    }

    @Transactional
    public boolean validatePlayGroundNodAccess(String email) {
        UserEntity user = (UserEntity) userService.loadUserByUsername(email);
        SubscriptionPlanEntity activePlan = subscriptionPlanRepository.getBySubscriptions_User(user);
        int maxNodAllow = activePlan.getMaxNumNode();
        int applianceCount = (int) applianceRepository.countByUserEmail(email);
        int sectionCount = (int) sectionRepository.countByUserEmail(email);
        int currentNodCount = applianceCount + sectionCount;
        return (currentNodCount < maxNodAllow);
    }





}
