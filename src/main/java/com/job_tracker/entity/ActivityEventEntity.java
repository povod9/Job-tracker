package com.job_tracker.entity;

import com.job_tracker.enums.ActivityEventType;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "activity_events")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ActivityEventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "application_id", nullable = false)
  private ApplicationEntity application;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ActivityEventType type;

  @Column(name = "created_at", nullable = false)
  @CreationTimestamp
  private OffsetDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", nullable = false)
  private UserEntity user;
}
