package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.TariffEntity;
import com.greenbill.greenbill.enumeration.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TariffRepository extends JpaRepository<TariffEntity, Long> {
    List<TariffEntity> getByLimitedFromLessThanEqualAndLimitedToGreaterThanEqualAndCategoryOrderByLowerLimitAsc(double limitedFrom, double limitedTo, ProjectType category);
}