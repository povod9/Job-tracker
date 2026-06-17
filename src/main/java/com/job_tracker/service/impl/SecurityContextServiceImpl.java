package com.job_tracker.service.impl;

import com.job_tracker.create_exception.AccessDeniedException;
import com.job_tracker.create_exception.InvalidCredentialsException;
import com.job_tracker.dto.PrincipalDto;
import com.job_tracker.service.SecurityContextService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextServiceImpl implements SecurityContextService {
  @Override
  public PrincipalDto getCurrentPrincipalOrThrow() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getPrincipal() == null) {
      throw new InvalidCredentialsException("You are unauthorized");
    }
    return (PrincipalDto) authentication.getPrincipal();
  }

  @Override
  public void validateOwnershipOrThrow(Long id) {
    PrincipalDto principalDto = getCurrentPrincipalOrThrow();
    if (!principalDto.id().equals(id)) {
      throw new AccessDeniedException("You do not have permission to modify or view this resource");
    }
  }
}
