package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.ApplianceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplianceRepository extends JpaRepository<ApplianceEntity, Long> {
    long countByFrontEndIdContains(String frontEndId);
    ApplianceEntity findByFrontEndId(String frontEndId);


}