package com.job_tracker.repository;

import com.job_tracker.entity.VacancyEntity;
import com.job_tracker.enums.VacancyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacancyRepository extends JpaRepository<VacancyEntity, Long> {
    List<VacancyEntity> findAllByUserIdAndStatus(Long id, VacancyStatus status);
    List<VacancyEntity> findAllByUserIdAndStatusNot(Long id, VacancyStatus status);

}
