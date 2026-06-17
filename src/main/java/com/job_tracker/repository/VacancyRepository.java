package com.job_tracker.repository;

import com.job_tracker.entity.VacancyEntity;
import com.job_tracker.enums.VacancyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<VacancyEntity, Long> {
  Page<VacancyEntity> findAllByUserIdAndStatus(Long id, VacancyStatus status, Pageable pageable);

  Page<VacancyEntity> findAllByUserIdAndStatusNot(Long id, VacancyStatus status, Pageable pageable);
}
