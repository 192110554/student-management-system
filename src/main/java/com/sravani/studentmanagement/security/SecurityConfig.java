package com.sravani.studentmanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.sravani.studentmanagement.service.CustomUserDetailsService;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/login",
                                "/api/register",
                                "/api/upload",
                                "/css/**",
                                "/js/**",
                                "/uploads/**")
                        .permitAll()

                        .requestMatchers(org.springframework.http.HttpMethod.GET,
                                "/api/courses")
                        .hasAnyRole("ADMIN", "USER")

                        .requestMatchers(org.springframework.http.HttpMethod.POST,
                                "/api/courses")
                        .hasRole("ADMIN")

                        .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                "/api/courses/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/students/export/**")
                        .hasRole("ADMIN")

                        .requestMatchers(org.springframework.http.HttpMethod.POST,
                                "/api/students")
                        .hasRole("ADMIN")

                        .requestMatchers(org.springframework.http.HttpMethod.PUT,
                                "/api/students/**")
                        .hasRole("ADMIN")

                        .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                "/api/students/**")
                        .hasRole("ADMIN")

                        .requestMatchers(org.springframework.http.HttpMethod.GET,
                                "/api/students/**")
                        .hasAnyRole("ADMIN", "USER")

                        .requestMatchers(org.springframework.http.HttpMethod.GET,
                                "/api/attendance/**")
                        .hasAnyRole("ADMIN", "USER")

                        .requestMatchers(org.springframework.http.HttpMethod.POST,
                                "/api/attendance/**")
                        .hasRole("ADMIN")

                        .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                "/api/attendance/**")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}