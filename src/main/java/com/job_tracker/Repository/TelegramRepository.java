package com.job_tracker.Repository;

import com.job_tracker.Entity.TelegramSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramRepository extends JpaRepository<TelegramSettingEntity, Long> {
}
