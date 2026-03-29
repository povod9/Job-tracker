package com.job_tracker.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationFilter authenticationFilter) throws Exception {
        http
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                        .requestMatchers("/user/me/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin/find/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/admin/delete/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin/deleted/application").hasRole("ADMIN")
                        .requestMatchers("/application/me/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/reminder/me/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
