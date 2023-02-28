package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.AddProjectReqResDto;
import com.greenbill.greenbill.dto.AddSectionDto;
import com.greenbill.greenbill.entity.ProjectEntity;
import com.greenbill.greenbill.entity.SectionEntity;
import com.greenbill.greenbill.repository.ApplianceRepository;
import com.greenbill.greenbill.repository.ProjectRepository;
import com.greenbill.greenbill.repository.SectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayGroundService {
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ApplianceRepository applianceRepository;

    @Transactional
    public void addSection(AddSectionDto addSectionDto) {
        SectionEntity Ssection = new SectionEntity();
        if (addSectionDto.getParentSectionId() == null) {
            SectionEntity section = new SectionEntity(addSectionDto);
            Ssection = sectionRepository.save(section);
        } else {
            SectionEntity parentSection = sectionRepository.getFirstById(addSectionDto.getParentSectionId());
            SectionEntity section = new SectionEntity(addSectionDto);
            section.setParentSection(parentSection);
            Ssection = sectionRepository.save(section);
            for (SectionEntity sec : parentSection.getChildSections()
            ) {
                System.out.println(sec.getName());
            }
        }
    }

    public AddProjectReqResDto addProject(AddProjectReqResDto addProjectReqResDto) throws Exception {
        ProjectEntity project = new ProjectEntity(addProjectReqResDto);
        return new AddProjectReqResDto(projectRepository.save(project));
    }


}
