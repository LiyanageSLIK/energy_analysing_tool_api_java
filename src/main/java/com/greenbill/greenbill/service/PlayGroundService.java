package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.AddProjectReqResDto;
import com.greenbill.greenbill.dto.CommonNodReqDto;
import com.greenbill.greenbill.dto.NodDeleteReqDto;
import com.greenbill.greenbill.entity.*;
import com.greenbill.greenbill.enumerat.NodType;
import com.greenbill.greenbill.repository.ApplianceRepository;
import com.greenbill.greenbill.repository.ProjectRepository;
import com.greenbill.greenbill.repository.SectionRepository;
import com.greenbill.greenbill.repository.SubscriptionPlanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayGroundService {
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private ApplianceRepository applianceRepository;
    @Autowired
    private UserService userService;


    @Transactional
    public AddProjectReqResDto addProject(AddProjectReqResDto addProjectReqResDto, String userEmail) throws Exception {
        if (!validatePlayGroundProjectAccess(userEmail)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Sorry You had reach your subscription limitations upgrade your plan for more benefits");
        }
        UserEntity user = (UserEntity) userService.loadUserByUsername(userEmail);
        ProjectEntity project = new ProjectEntity(addProjectReqResDto);
        project.setUser(user);
        return new AddProjectReqResDto(projectRepository.save(project));
    }

    public List<AddProjectReqResDto> getAllProject(String userEmail) throws Exception {
        List<ProjectEntity> projecList = projectRepository.getByUser_EmailOrderByLastUpdatedDesc(userEmail);
        List<AddProjectReqResDto> dtoList = new ArrayList<>();
        for (ProjectEntity project : projecList) {
            dtoList.add(new AddProjectReqResDto(project));
        }
        return dtoList;
    }

    @Transactional
    public AddProjectReqResDto updateProject(AddProjectReqResDto addProjectReqResDto) throws Exception {
        ProjectEntity project = projectRepository.getFirstById(addProjectReqResDto.getProjectId());
        project.update(addProjectReqResDto);
        return new AddProjectReqResDto(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(long projectId) {
        projectRepository.removeById(projectId);
    }

    @Transactional
    public void addNod(CommonNodReqDto commonNodReqDto, String userEmail) throws Exception {
        if (!validatePlayGroundNodAccess(userEmail)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Sorry You had reach your subscription limitations upgrade your plan for more benefits");
        }
        NodType nodType = commonNodReqDto.getNodType();
        ProjectEntity project = projectRepository.getFirstById(commonNodReqDto.getProjectId());
        if (nodType == NodType.SECTION) {
            SectionEntity savedSection = new SectionEntity();
            if (commonNodReqDto.getParentNodId().equals("root")) {
                SectionEntity section = new SectionEntity(commonNodReqDto);
                project.setLastUpdated();
                section.setProject(project);
                section.setUserEmail(userEmail);
                savedSection = sectionRepository.save(section);
            } else {
                String parentNodId = commonNodReqDto.getParentNodId();
                long referenceProjectId = commonNodReqDto.getProjectId();
                SectionEntity parentSection = sectionRepository.findByReferenceProjectIdAndNodeId(referenceProjectId, parentNodId);
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
        if (nodType == NodType.APPLIANCE) {
            ApplianceEntity savedAppliance = new ApplianceEntity();
            String parentNodId = commonNodReqDto.getParentNodId();
            long referenceProjectId = commonNodReqDto.getProjectId();
            SectionEntity parentSection = sectionRepository.findByReferenceProjectIdAndNodeId(referenceProjectId, parentNodId);
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
        NodType nodType = commonNodReqDto.getNodType();
        ProjectEntity project = projectRepository.getFirstById(commonNodReqDto.getProjectId());
        if (nodType == NodType.SECTION) {
            String nodId = commonNodReqDto.getNodeId();
            long referenceProjectId = commonNodReqDto.getProjectId();
            SectionEntity thisSection = sectionRepository.findByNodeIdAndReferenceProjectId(nodId, referenceProjectId);
            if (thisSection == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Section not found");
            } else {
                thisSection.updateSection(commonNodReqDto);
                project.setLastUpdated();
                projectRepository.save(project);
                thisSection = sectionRepository.save(thisSection);
            }
        }
        if (nodType == NodType.APPLIANCE) {
            String nodId = commonNodReqDto.getNodeId();
            long referenceProjectId = commonNodReqDto.getProjectId();
            ApplianceEntity thisAppliance = applianceRepository.findByNodeIdAndReferenceProjectId(nodId, referenceProjectId);
            if (thisAppliance == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Appliance not found");
            } else {
                thisAppliance.updateSection(commonNodReqDto);
                project.setLastUpdated();
                projectRepository.save(project);
                thisAppliance = applianceRepository.save(thisAppliance);
            }
        }
    }

    public void deleteNod(NodDeleteReqDto nodDeleteReqDto) {
        SectionEntity section = sectionRepository.findByReferenceProjectIdAndNodeId(nodDeleteReqDto.getProjectId(), nodDeleteReqDto.getNodId());
        ApplianceEntity appliance = applianceRepository.findByReferenceProjectIdAndNodeId(nodDeleteReqDto.getProjectId(), nodDeleteReqDto.getNodId());
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
