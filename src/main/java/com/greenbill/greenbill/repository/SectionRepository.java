package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.SectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<SectionEntity, Long> {
    SectionEntity getFirstById(Long id);
    @Override
    Optional<SectionEntity> findById(Long aLong);
}