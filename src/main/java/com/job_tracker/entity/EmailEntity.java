package com.job_tracker.entity;

import com.job_tracker.enums.EmailStatus;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "email_queue")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "reminder_id", nullable = false)
  private ReminderEntity reminder;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private EmailStatus status;

  @Column(name = "created_at", nullable = false)
  @CreationTimestamp
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  @UpdateTimestamp
  private OffsetDateTime updatedAt;

  @Column(nullable = false)
  private Integer attempts;

  @Column(name = "error_message")
  private String errorMessage;
}
