package com.job_tracker.repository;

import com.job_tracker.entity.ReminderEntity;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {

  Page<ReminderEntity> findAllByUserId(Long userId, Pageable pageable);

  @EntityGraph(attributePaths = {"application", "application.vacancy"})
  Optional<ReminderEntity> findById(Long id);

  @Query("""
      SELECT r FROM ReminderEntity r
      LEFT JOIN EmailEntity e ON r.id = e.reminder.id
      WHERE r.dueAt < :now
      AND NOT EXISTS(
      SELECT 1 FROM EmailEntity e WHERE e.reminder.id = r.id
      )
""")
  @EntityGraph(attributePaths = {"user", "application", "application.vacancy"})
  List<ReminderEntity> findReminderWithoutEmail(@Param("now") OffsetDateTime time);
}
