package com.job_tracker.Repository;

import com.job_tracker.Entity.ApplicationEntity;
import com.job_tracker.Enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    List<ApplicationEntity> findByUserIdAndApplicationStatusNot(Long userId, ApplicationStatus applicationStatus);
    List<ApplicationEntity> findByApplicationStatus(ApplicationStatus applicationStatus);
}
