package com.job_tracker.repository;

import com.job_tracker.entity.ApplicationEntity;
import com.job_tracker.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    List<ApplicationEntity> findByUserIdAndApplicationStatusNot(Long userId, ApplicationStatus applicationStatus);
    List<ApplicationEntity> findByApplicationStatus(ApplicationStatus applicationStatus);
    List<ApplicationEntity> findByUserId(Long userId);

    boolean existsByUserIdAndCompanyAndPosition(Long userId, String company, String position);
}
