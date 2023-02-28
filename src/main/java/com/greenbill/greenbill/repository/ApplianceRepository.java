package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.ApplianceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplianceRepository extends JpaRepository<ApplianceEntity, Long> {
}