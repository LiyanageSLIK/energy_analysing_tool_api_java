package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.TreeViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreeViewRepository extends JpaRepository<TreeViewEntity, Long> {
    TreeViewEntity findByProject_Id(Long id);
}