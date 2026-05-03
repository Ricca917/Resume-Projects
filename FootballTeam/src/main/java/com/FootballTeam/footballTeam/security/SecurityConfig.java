package com.FootballTeam.footballTeam.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private JwtAuthEntryPoint authEntryPoint;
    private CustomUserDetailsService userDetailsService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoint pubblici che non richiedono autenticazione (login e registrazione)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // Endpoint per visualizzare (GET) giocatori, squadre, leghe, contratti - Accessibili a USER e ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/players/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/teams/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/leagues/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/contracts/**").hasAnyRole("USER", "ADMIN")

                        // Endpoint per visualizzare gli utenti - Accessibile SOLO ad ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")

                        // Endpoint per creazione, modifica, eliminazione - Accessibili SOLO ad ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/players").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/players/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/players/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/teams").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/teams/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/teams/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/leagues").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/leagues/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/leagues/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/contracts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/contracts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/contracts/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}