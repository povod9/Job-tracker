package com.job_tracker.repository;

import com.job_tracker.entity.VacancyEntity;
import com.job_tracker.enums.VacancyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface VacancyRepository extends JpaRepository<VacancyEntity, Long> {
  Page<VacancyEntity> findAllByStatus(VacancyStatus status, Pageable pageable);

  Page<VacancyEntity> findAllByStatusNot(VacancyStatus status, Pageable pageable);

  @Query(
      """
        SELECT v FROM VacancyEntity v
        WHERE v.externalId in :incomingIds
""")
  List<String> findAllExternalIdsByExternalIdIn(List<String> incomingIds);
}
