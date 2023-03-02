package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.SectionEntity;
import com.greenbill.greenbill.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, Long> {
    long countByUserEmail(String userEmail);

    @Query("select count(s) from SectionEntity s where s.project.user = ?1")
    long countByProject_User(UserEntity user);

    SectionEntity findByNodeIdAndReferenceProjectId(String nodeId, Long referenceProjectId);

    SectionEntity findByReferenceProjectIdAndNodeId(Long referenceProjectId, String nodeId);

    SectionEntity getByNodeIdAndProject_Id(String nodeId, Long id);

    SectionEntity findFirstByParentNodIdAndProject_Id(String parentNodId, Long id);

    SectionEntity getFirstById(Long id);

    @Override
    Optional<SectionEntity> findById(Long aLong);
}