package com.job_tracker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppSettingsEntity {

    @Id
    @Column(name = "app_key")
    private String appKey;

    @Column(name = "app_value", nullable = false)
    private String appValue;
}
