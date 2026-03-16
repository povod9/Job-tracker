package com.job_tracker.Repository;

import com.job_tracker.Entity.ReminderEntity;
import com.job_tracker.Enums.ReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;


@Repository
public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {

    List<ReminderEntity> findByStatus(ReminderStatus status);

    List<ReminderEntity> findByDueAtBefore(OffsetDateTime dueAtBefore);
}
