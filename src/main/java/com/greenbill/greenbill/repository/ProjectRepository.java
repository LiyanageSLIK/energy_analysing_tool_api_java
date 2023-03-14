package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.ProjectEntity;
import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.enumeration.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    long countBySubscription_User_Email(String email);
    List<ProjectEntity> getBySubscription_StatusAndSubscription_User_EmailOrderByLastUpdatedDesc(Status status, String email);
    long removeById(Long id);
    ProjectEntity getFirstById(Long id);

}