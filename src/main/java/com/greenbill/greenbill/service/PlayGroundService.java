package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.AddProjectReqResDto;
import com.greenbill.greenbill.dto.AddSectionDto;
import com.greenbill.greenbill.dto.CommonNodReqDto;
import com.greenbill.greenbill.entity.ApplianceEntity;
import com.greenbill.greenbill.entity.ProjectEntity;
import com.greenbill.greenbill.entity.SectionEntity;
import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.enumerat.NodType;
import com.greenbill.greenbill.repository.ApplianceRepository;
import com.greenbill.greenbill.repository.ProjectRepository;
import com.greenbill.greenbill.repository.SectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayGroundService {
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ApplianceRepository applianceRepository;
    @Autowired
    private UserService userService;



    @Transactional
    public AddProjectReqResDto addProject(AddProjectReqResDto addProjectReqResDto,String userEmail) throws Exception {
        UserEntity user= (UserEntity) userService.loadUserByUsername(userEmail);
        ProjectEntity project = new ProjectEntity(addProjectReqResDto);
        project.setUser(user);
        return new AddProjectReqResDto(projectRepository.save(project));
    }

    public List<AddProjectReqResDto> getAllProject( String userEmail) throws Exception {
        List<ProjectEntity> projecList=projectRepository.getByUser_EmailOrderByLastUpdatedDesc(userEmail);
        List<AddProjectReqResDto> dtoList=new ArrayList<>();
        for (ProjectEntity project:projecList) {
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
    public void deleteProject(long projectId){
        projectRepository.removeById(projectId);
    }
    @Transactional
    public void addNod (CommonNodReqDto commonNodReqDto){
        NodType nodType=commonNodReqDto.getNodType();
        if(nodType==NodType.SECTION){
            SectionEntity savedSection = new SectionEntity();
            if (commonNodReqDto.getParentNodId() == null) {
                SectionEntity section = new SectionEntity(commonNodReqDto);
                savedSection = sectionRepository.save(section);
            } else {
                String parentNodId=commonNodReqDto.getParentNodId();
                long projectId=commonNodReqDto.getProjectId();
                SectionEntity parentSection = sectionRepository.findFirstByParentNodIdAndProject_Id(parentNodId,projectId);
                SectionEntity section = new SectionEntity(commonNodReqDto);
                section.setParentSection(parentSection);
                savedSection = sectionRepository.save(section);
            }
        }
        if(nodType==NodType.APPLIANCE){
            ApplianceEntity savedAppliance=new ApplianceEntity();
            String parentNodId=commonNodReqDto.getParentNodId();
            long projectId=commonNodReqDto.getProjectId();
            SectionEntity parentSection = sectionRepository.findFirstByParentNodIdAndProject_Id(parentNodId,projectId);
            ApplianceEntity appliance=new ApplianceEntity();
        }
    }

}
