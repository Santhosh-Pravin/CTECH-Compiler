package com.ctechcompiler.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Use Lombok to inject the dependencies
public class SecurityConfig {

    // These will be injected by Spring
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000")); // Add frontend URL if you have one
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(Arrays.asList("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                // Disable CSRF as it's not needed for a stateless JWT API
                .csrf(AbstractHttpConfigurer::disable)

                // Define authorization rules for different endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Anyone can login/register
                        .requestMatchers("/api/mentor/**").hasAuthority("MENTOR") // Only teachers access mentor endpoints
                        .requestMatchers("/api/student/**").hasAuthority("STUDENT") // Only students access student endpoints
                        .requestMatchers("/api/viva/**").hasAnyAuthority("MENTOR", "STUDENT") // Both can use viva
                        .anyRequest().authenticated() // All other requests need authentication
                )

                // Configure session management to be STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Set the custom authentication provider
                .authenticationProvider(authenticationProvider)

                // Add the JWT filter to be executed before the standard username/password filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // We REMOVED the .formLogin() and .logout() configurations as they are not needed.
        // Your AuthController will handle login, and logout on the client-side is simply deleting the token.

        return http.build();
    }
}


