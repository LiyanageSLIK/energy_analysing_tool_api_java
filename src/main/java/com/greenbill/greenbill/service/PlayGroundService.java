package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.AddProjectReqResDto;
import com.greenbill.greenbill.dto.AddSectionDto;
import com.greenbill.greenbill.entity.ProjectEntity;
import com.greenbill.greenbill.entity.SectionEntity;
import com.greenbill.greenbill.entity.UserEntity;
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
    public void addSection(AddSectionDto addSectionDto) {
        SectionEntity Ssection = new SectionEntity();
        if (addSectionDto.getParentNodId() == null) {
            SectionEntity section = new SectionEntity(addSectionDto);
            Ssection = sectionRepository.save(section);
        } else {
            SectionEntity parentSection = sectionRepository.findFirstByParentNodIdAndProject_Id(addSectionDto.getParentNodId(), addSectionDto.getProjectId());
            SectionEntity section = new SectionEntity(addSectionDto);
            section.setParentSection(parentSection);
            Ssection = sectionRepository.save(section);
            for (SectionEntity sec : parentSection.getChildSections()
            ) {
                System.out.println(sec.getName());
            }
        }
    }
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

}
