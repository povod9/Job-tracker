package com.job_tracker.repository;

import com.job_tracker.entity.ReminderEntity;
import com.job_tracker.enums.ReminderStatus;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {

  List<ReminderEntity> findByStatus(ReminderStatus status);

  List<ReminderEntity> findAllByUserId(Long userId);

  List<ReminderEntity> findByDueAtBefore(OffsetDateTime dueAtBefore);
}
