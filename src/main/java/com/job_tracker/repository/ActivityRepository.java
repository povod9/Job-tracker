package com.job_tracker.repository;

import com.job_tracker.entity.ActivityEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEventEntity, Long> {

    List<ActivityEventEntity> findByApplicationId(Long applicationId);
}
