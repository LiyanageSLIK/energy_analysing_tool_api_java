package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.ProjectEntity;
import com.greenbill.greenbill.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    long countByUser(UserEntity user);

    long removeById(Long id);

    long deleteByIdAllIgnoreCase(Long id);

    ProjectEntity getFirstById(Long id);

    List<ProjectEntity> getByUser_EmailOrderByLastUpdatedDesc(String email);
}