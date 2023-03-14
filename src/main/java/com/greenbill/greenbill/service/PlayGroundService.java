package com.greenbill.greenbill.service;


import com.greenbill.greenbill.dto.ApplianceDto;
import com.greenbill.greenbill.dto.NodeDto;
import com.greenbill.greenbill.dto.SectionDto;
import com.greenbill.greenbill.entity.*;
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
    public void addNode(NodeDto nodeDto, String userEmail) throws Exception {
        if (!validatePlayGroundNodAccess(userEmail)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Sorry You had reach your subscription limitations upgrade your plan for more benefits");
        }
        NodeType nodeType = nodeDto.getNodeType();
        long projectId=extractProjectIdFromFrontEndId(nodeDto.getFrontEndId());
        var root=rootRepository.findByProject_Id(projectId);
        var project=projectRepository.getReferenceById(projectId);
        if (nodeType == NodeType.SECTION) {
            SectionEntity savedSection = new SectionEntity();
            if (nodeDto.getParentFrontEndId().equals("root")) {
                SectionEntity sectionToSave = new SectionEntity((SectionDto) nodeDto);
                project.setLastUpdated(new Date());
                root.setProject(project);
                sectionToSave.setParent(root);
                savedSection = sectionRepository.save(sectionToSave);
            } else {
                String parentFrontEndId = nodeDto.getParentFrontEndId();
                SectionEntity parentSection = sectionRepository.findByFrontEndId(parentFrontEndId);
                if (parentSection == null) {
                    throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Cant map Nod:Parent Nod not Found ");
                }
                SectionEntity sectionToSave = new SectionEntity((SectionDto) nodeDto);
                sectionToSave.setParent(parentSection);
                project.setLastUpdated(new Date());
                projectRepository.save(project);
                savedSection = sectionRepository.save(sectionToSave);
            }
        }
        if (nodeType == NodeType.APPLIANCE) {
            ApplianceEntity savedAppliance = new ApplianceEntity();
            String parentFrontEndId = nodeDto.getParentFrontEndId();
            SectionEntity parentSection = sectionRepository.findByFrontEndId(parentFrontEndId);
            if (parentSection == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Cant map Nod:Parent Nod not Found ");
            }
            ApplianceEntity applianceToSave = new ApplianceEntity((ApplianceDto) nodeDto);
            applianceToSave.setParent(parentSection);
            project.setLastUpdated(new Date());
            projectRepository.save(project);
            savedAppliance = applianceRepository.save(applianceToSave);
        }
    }

    @Transactional
    public void updateNode(NodeDto nodeDto) throws Exception {
        NodeType nodeType = nodeDto.getNodeType();
        long projectId=extractProjectIdFromFrontEndId(nodeDto.getFrontEndId());
        var project=projectRepository.getReferenceById(projectId);
        if (nodeType == NodeType.SECTION) {
            String frontEndId= nodeDto.getFrontEndId();
            var thisSection = sectionRepository.findByFrontEndId(frontEndId);
            if (thisSection == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "SectionDto not found");
            } else {
                thisSection.update((SectionDto)nodeDto);
                project.setLastUpdated(new Date());
                projectRepository.save(project);
                thisSection = sectionRepository.save(thisSection);
            }
        }
        if (nodeType == NodeType.APPLIANCE) {
            String frontEndId= nodeDto.getFrontEndId();
            var thisAppliance = applianceRepository.findByFrontEndId(frontEndId);
            if (thisAppliance == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ApplianceDto not found");
            } else {
                thisAppliance.update((ApplianceDto)nodeDto);
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
        String userId=userRepository.findByEmail(email).getId().toString();
        int maxNodAllow = activePlan.getMaxNumNode();
        int applianceCount = (int) applianceRepository.countByFrontEndIdContains(userId);
        int sectionCount = (int) sectionRepository.countByFrontEndIdContains(userId);
        int currentNodCount = applianceCount + sectionCount;
        return (currentNodCount < maxNodAllow);
    }

    private long extractProjectIdFromFrontEndId(String frontEndId){
        String projectId=frontEndId.split("_")[1];
        return Long.parseLong(projectId);
    }






}
