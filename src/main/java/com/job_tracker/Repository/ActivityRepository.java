package com.job_tracker.Repository;

import com.job_tracker.Entity.ActivityEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEventEntity, Long> {
    List<ActivityEventEntity> findByApplicationId(Long applicationId);
}
