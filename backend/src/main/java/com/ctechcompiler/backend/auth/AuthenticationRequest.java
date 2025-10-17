package com.ctechcompiler.backend.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The standard DTO for carrying user credentials.
 * @Data provides getters, setters, toString(), etc.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String password;
    private String name; // This field is used for mentor registration
}