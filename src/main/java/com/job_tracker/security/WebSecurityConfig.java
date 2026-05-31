package com.job_tracker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, AuthenticationFilter authenticationFilter) throws Exception {
    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            (authorizeRequests) ->
                authorizeRequests
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/user/register",
                                "/user/login"
                            ).permitAll()
                        .requestMatchers("/vacancy/**").hasRole("HR")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/hr").hasAnyRole("ADMIN", "HR")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "HR")
                    .anyRequest()
                    .authenticated());
    return http.build();
  }
}
