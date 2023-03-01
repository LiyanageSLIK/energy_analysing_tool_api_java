package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    long removeById(Long id);
    long deleteByIdAllIgnoreCase(Long id);
    ProjectEntity getFirstById(Long id);
    List<ProjectEntity> getByUser_EmailOrderByLastUpdatedDesc(String email);
}