package com.job_tracker.service;

import com.job_tracker.dto.PrincipalDto;

public interface SecurityContextService {
  PrincipalDto getCurrentPrincipalOrThrow();

  void validateOwnershipOrThrow(Long id);
}
