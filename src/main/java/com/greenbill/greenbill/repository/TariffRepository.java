package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.TariffEntity;
import com.greenbill.greenbill.enumeration.ProjectType;
import com.greenbill.greenbill.enumeration.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TariffRepository extends JpaRepository<TariffEntity, Long> {
    List<TariffEntity> getByLimitedFromLessThanEqualAndLimitedToGreaterThanEqualAndCategoryAndStatusOrderByLowerLimitAsc(double limitedFrom, double limitedTo, ProjectType category, Status status);
    @Transactional
    @Modifying
    @Query("update TariffEntity t set t.status = ?1 where t.status = ?2")
    int updateStatusByStatus(Status status, Status status1);

}