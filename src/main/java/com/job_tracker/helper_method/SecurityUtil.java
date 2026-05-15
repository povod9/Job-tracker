package com.job_tracker.helper_method;

import com.job_tracker.create_exception.InvalidCredentialsException;
import com.job_tracker.dto.PrincipalDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

  public static PrincipalDto getCurrentPrincipalOrThrow() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getPrincipal() == null) {
      throw new InvalidCredentialsException("You are unauthorized");
    }
    return (PrincipalDto) authentication.getPrincipal();
  }
}
