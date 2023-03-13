package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.RootEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RootRepository extends JpaRepository<RootEntity, Long> {
    RootEntity findByProject_Id(Long id);
}