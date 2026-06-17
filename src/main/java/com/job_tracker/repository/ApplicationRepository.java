package com.job_tracker.repository;

import com.job_tracker.entity.ApplicationEntity;
import com.job_tracker.enums.ApplicationStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

  @EntityGraph(attributePaths = {"user", "vacancy"})
  List<ApplicationEntity> findByUserIdAndApplicationStatusNot(
      Long userId, ApplicationStatus applicationStatus);

  List<ApplicationEntity> findByApplicationStatus(ApplicationStatus applicationStatus);

  Page<ApplicationEntity> findAllByUserId(Long id, Pageable pageable);

  @EntityGraph(attributePaths = {"vacancy"})
  @Override
  Optional<ApplicationEntity> findById(Long id);
}
