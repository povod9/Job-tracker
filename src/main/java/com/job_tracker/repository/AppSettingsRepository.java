package com.job_tracker.repository;

import com.job_tracker.entity.AppSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppSettingsRepository extends JpaRepository<AppSettingsEntity, String> {
}
