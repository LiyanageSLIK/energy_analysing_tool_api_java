package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.RootEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RootRepository extends JpaRepository<RootEntity, Long> {
    List<RootEntity> findByProject_Subscription_User_Email(String email);

    RootEntity findByProject_Id(Long id);
}