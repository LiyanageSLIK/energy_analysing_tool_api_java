package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.SectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, Long> {
    long countByFrontEndIdContains(String frontEndId);
    SectionEntity findByFrontEndId(String frontEndId);


}