package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.SectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, Long> {
    long countByUserEmail(String userEmail);


    SectionEntity findByNodIdAndReferenceProjectId(String nodId, Long referenceProjectId);

    SectionEntity findByReferenceProjectIdAndNodId(Long referenceProjectId, String nodId);


    @Override
    Optional<SectionEntity> findById(Long aLong);
}