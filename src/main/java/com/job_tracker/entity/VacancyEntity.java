package com.job_tracker.entity;

import com.job_tracker.enums.VacancySource;
import com.job_tracker.enums.VacancyStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "vacancies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacancyEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "external_id", unique = true)
  private String externalId;

  @Column(nullable = false)
  private String company;

  @Column(nullable = false)
  private String position;

  @Column(nullable = false)
  private String description;

  @Column
  @JdbcTypeCode(SqlTypes.ARRAY)
  private List<String> location;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = true)
  private UserEntity user;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private VacancyStatus status;

  @Column(name = "source", nullable = false)
  @Enumerated(EnumType.STRING)
  private VacancySource source;

  @Column(name = "created_at", nullable = false)
  @CreationTimestamp
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  @UpdateTimestamp
  private OffsetDateTime updatedAt;

  @Column(name = "version", nullable = false)
  @Version
  private Long version;

  @Column(name = "salary_max")
  private BigDecimal salaryMax;

  @Column(name = "salary_min")
  private BigDecimal salaryMin;

  @Column(name = "redirect_url")
  private String redirectURL;
}
