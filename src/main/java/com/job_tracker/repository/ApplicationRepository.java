package com.job_tracker.repository;

import com.job_tracker.entity.ApplicationEntity;
import com.job_tracker.enums.ApplicationStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

  List<ApplicationEntity> findByUserIdAndApplicationStatusNot(
      Long userId, ApplicationStatus applicationStatus);

  List<ApplicationEntity> findByApplicationStatus(ApplicationStatus applicationStatus);

  List<ApplicationEntity> findByUserId(Long userId);

}
