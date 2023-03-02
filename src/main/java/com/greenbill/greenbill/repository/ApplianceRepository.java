package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.ApplianceEntity;
import com.greenbill.greenbill.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplianceRepository extends JpaRepository<ApplianceEntity, Long> {
    ApplianceEntity findByReferenceProjectIdAndNodeId(Long referenceProjectId, String nodeId);

    long countByUserEmail(String userEmail);

    @Query("select count(a) from ApplianceEntity a where a.section.project.user = ?1")
    long countBySection_Project_User(UserEntity user);

    ApplianceEntity findByNodeIdAndReferenceProjectId(String nodeId, Long referenceProjectId);


}