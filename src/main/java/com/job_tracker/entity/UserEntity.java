package com.job_tracker.entity;

import com.job_tracker.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.time.OffsetDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  @Email
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role = Role.USER;

  @Column(name = "created_at", nullable = false)
  @CreationTimestamp
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  @UpdateTimestamp
  private OffsetDateTime updatedAt;
}
