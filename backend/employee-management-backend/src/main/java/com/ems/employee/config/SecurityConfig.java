package com.ems.employee.config;

import com.ems.employee.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter
            jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter
                    jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(
                        csrf ->
                                csrf.disable()
                )

                .cors(
                        Customizer.withDefaults()
                )

                .sessionManagement(
                        session ->
                                session
                                        .sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS
                                        )
                )

                .authorizeHttpRequests(
                        auth ->
                                auth

                                        .requestMatchers(
                                                "/api/auth/**"
                                        )
                                        .permitAll()


                                        .requestMatchers(
                                                HttpMethod.GET,
                                                "/api/audit/**"
                                        )
                                        .hasRole("ADMIN")


                                        .requestMatchers(
                                                HttpMethod.GET,
                                                "/api/employees/**"
                                        )
                                        .hasAnyRole(
                                                "ADMIN",
                                                "USER"
                                        )

                                        .requestMatchers(
                                                HttpMethod.POST,
                                                "/api/employees/**"
                                        )
                                        .hasRole(
                                                "ADMIN"
                                        )

                                        .requestMatchers(
                                                HttpMethod.PUT,
                                                "/api/employees/**"
                                        )
                                        .hasRole(
                                                "ADMIN"
                                        )

                                        .requestMatchers(
                                                HttpMethod.DELETE,
                                                "/api/employees/**"
                                        )
                                        .hasRole(
                                                "ADMIN"
                                        )

                                        .anyRequest()
                                        .authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}