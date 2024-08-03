package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.response.SolarPanelDto;
import com.greenbill.greenbill.entity.ProjectEntity;
import com.greenbill.greenbill.entity.SolarPanelEntity;
import com.greenbill.greenbill.repository.ProjectRepository;
import com.greenbill.greenbill.repository.SolarPanelRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class SolarPanelService {

    @Autowired
    private SolarPanelRepository solarPanelRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public List<SolarPanelDto> getAllSolarPanelsForProject(long projectId) throws Exception {
        List<SolarPanelEntity> projecList = solarPanelRepository.findByProject_Id(projectId);
        return projecList.stream().map(SolarPanelDto::new).toList();
    }

    public void addSolarPanel(SolarPanelDto panelDto) {
        SolarPanelEntity solarPanel = new SolarPanelEntity(panelDto);
        ProjectEntity projectEntity = projectRepository.getById(panelDto.getProjectId());
        if (projectEntity == null) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Sorry could not find the project");
        }
        solarPanel.setProject(projectEntity);
        solarPanelRepository.save(solarPanel);
    }

    public void deletePanel(long panelId) {
        solarPanelRepository.deleteById(panelId);
    }
}
