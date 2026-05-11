package com.job_tracker.repository;

import com.job_tracker.entity.TelegramSettingEntity;
import com.job_tracker.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramRepository extends JpaRepository<TelegramSettingEntity, Long> {

  Optional<TelegramSettingEntity> findByUser(UserEntity user);
}
