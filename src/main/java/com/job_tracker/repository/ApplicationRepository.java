package com.job_tracker.repository;

import com.job_tracker.entity.ApplicationEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

  Page<ApplicationEntity> findAllByUserId(Long id, Pageable pageable);

  @EntityGraph(attributePaths = {"vacancy"})
  @Override
  Optional<ApplicationEntity> findById(Long id);
}
