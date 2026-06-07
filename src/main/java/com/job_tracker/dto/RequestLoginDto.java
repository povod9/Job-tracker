package com.job_tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestLoginDto(@NotBlank @Email String email,
                              @NotBlank @Size(min = 5, max = 50,
                                      message = """
                                              Password cannot be smaller than 5 symbols
                                              and bigger than 50 symbols
                                              """) String password) {}
