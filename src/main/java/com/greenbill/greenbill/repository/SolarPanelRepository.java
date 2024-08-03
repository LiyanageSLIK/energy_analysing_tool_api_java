package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.SolarPanelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolarPanelRepository extends JpaRepository<SolarPanelEntity, Long> {

    List<SolarPanelEntity> findByProject_Id(Long projectId);

}
