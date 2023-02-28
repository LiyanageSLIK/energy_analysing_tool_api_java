package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
}