package com.greenbill.greenbill.service;


import com.greenbill.greenbill.dto.request.NodRequestDto;
import com.greenbill.greenbill.entity.ApplianceEntity;
import com.greenbill.greenbill.entity.SectionEntity;
import com.greenbill.greenbill.entity.SubscriptionPlanEntity;
import com.greenbill.greenbill.enumeration.NodeType;
import com.greenbill.greenbill.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

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
    private ApplianceRepository applianceRepository;
    @Autowired
    private UserRepository userRepository;


    @Transactional
    public void addNode(NodRequestDto nodRequestDto, String userEmail) throws Exception {
        if (!validatePlayGroundNodAccess(userEmail)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Sorry You had reach your subscription limitations upgrade your plan for more benefits");
        }
        NodeType nodeType = nodRequestDto.getNodeType();
        long projectId = extractProjectIdFromFrontEndId(nodRequestDto.getFrontEndId());
        var root = rootRepository.findByProject_Id(projectId);
        var project = projectRepository.getReferenceById(projectId);
        if (nodeType == NodeType.Section) {
            SectionEntity savedSection = new SectionEntity();
            if (nodRequestDto.getParentFrontEndId().equals("root")) {
                SectionEntity sectionToSave = new SectionEntity(nodRequestDto);
                project.setLastUpdated(new Date());
                root.setProject(project);
                sectionToSave.setParent(root);
                savedSection = sectionRepository.save(sectionToSave);
            } else {
                String parentFrontEndId = nodRequestDto.getParentFrontEndId();
                SectionEntity parentSection = sectionRepository.findByFrontEndId(parentFrontEndId);
                if (parentSection == null) {
                    throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Cant map Nod:Parent Nod not Found ");
                }
                SectionEntity sectionToSave = new SectionEntity(nodRequestDto);
                sectionToSave.setParent(parentSection);
                project.setLastUpdated(new Date());
                projectRepository.save(project);
                savedSection = sectionRepository.save(sectionToSave);
            }
        }
        if (nodeType == NodeType.Appliance) {
            ApplianceEntity savedAppliance = new ApplianceEntity();
            String parentFrontEndId = nodRequestDto.getParentFrontEndId();
            SectionEntity parentSection = sectionRepository.findByFrontEndId(parentFrontEndId);
            if (parentSection == null || parentFrontEndId.equals("root")) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Cant map Nod:Parent Nod not Found ");
            }
            ApplianceEntity applianceToSave = new ApplianceEntity(nodRequestDto);
            applianceToSave.setParent(parentSection);
            project.setLastUpdated(new Date());
            projectRepository.save(project);
            savedAppliance = applianceRepository.save(applianceToSave);
        }
    }

    @Transactional
    public void updateNode(NodRequestDto nodRequestDto) throws Exception {
        NodeType nodeType = nodRequestDto.getNodeType();
        long projectId = extractProjectIdFromFrontEndId(nodRequestDto.getFrontEndId());
        var project = projectRepository.getReferenceById(projectId);
        if (nodeType == NodeType.Section) {
            String frontEndId = nodRequestDto.getFrontEndId();
            var thisSection = sectionRepository.findByFrontEndId(frontEndId);
            if (thisSection == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "SectionDto not found");
            } else {
                thisSection.update(nodRequestDto);
                project.setLastUpdated(new Date());
                projectRepository.save(project);
                thisSection = sectionRepository.save(thisSection);
            }
        }
        if (nodeType == NodeType.Appliance) {
            String frontEndId = nodRequestDto.getFrontEndId();
            var thisAppliance = applianceRepository.findByFrontEndId(frontEndId);
            if (thisAppliance == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ApplianceDto not found");
            } else {
                thisAppliance.update(nodRequestDto);
                project.setLastUpdated(new Date());
                projectRepository.save(project);
                thisAppliance = applianceRepository.save(thisAppliance);
            }
        }
    }

    public void deleteNod(String frontEndNodId) {
        SectionEntity section = sectionRepository.findByFrontEndId(frontEndNodId);
        ApplianceEntity appliance = applianceRepository.findByFrontEndId(frontEndNodId);
        if (section != null) {
            sectionRepository.delete(section);
        }
        if (appliance != null) {
            applianceRepository.delete(appliance);
        }
    }


    @Transactional
    public boolean validatePlayGroundNodAccess(String email) {
        SubscriptionPlanEntity activePlan = subscriptionPlanRepository.getBySubscriptions_User_Email(email);
        int maxNodAllow = activePlan.getMaxNumNode();
        int currentNodCount = countNodeCountByUserEmail(email);
        return (currentNodCount < maxNodAllow);
    }

    private int countNodeCountByUserEmail(String email) {
        var rootList = rootRepository.findByProject_Subscription_User_Email(email);
        int counter = 0;
        if (rootList == null) {
            return counter;
        }
        for (var root : rootList) {
            var childSectionList = root.getChildren();
            if (childSectionList == null) {
                return counter;
            }
            for (var section : childSectionList) {
                counter = counter + countChildNod((SectionEntity) section);
            }
        }
        return counter;
    }

    private int countChildNod(SectionEntity sectionEntity) {
        int counter = 1;
        var children = sectionEntity.getChildren();
        for (var node : children) {
            if (node.getNodeType() == NodeType.Appliance) {
                counter = counter + 1;
            }
            if (node.getNodeType() == NodeType.Section) {
                counter = counter + countChildNod((SectionEntity) node);
            }
        }
        return counter;
    }

    private long extractProjectIdFromFrontEndId(String frontEndId) {
        String projectId = frontEndId.split("_")[1];
        return Long.parseLong(projectId);
    }

}
