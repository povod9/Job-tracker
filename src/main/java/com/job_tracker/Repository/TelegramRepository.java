package com.job_tracker.Repository;

import com.job_tracker.Entity.TelegramSettingEntity;
import com.job_tracker.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramRepository extends JpaRepository<TelegramSettingEntity, Long> {

    Optional<TelegramSettingEntity> findByUser(UserEntity user);
}
