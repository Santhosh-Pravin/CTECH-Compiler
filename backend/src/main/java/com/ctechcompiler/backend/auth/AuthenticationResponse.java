package com.ctechcompiler.backend.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the Data Transfer Object (DTO) sent back to the client
 * after a successful authentication. It contains the JWT token
 * and some basic user information.
 */
@Data // Generates getters, setters, toString(), equals(), and hashCode() methods
@Builder // Provides a builder pattern for object creation
@AllArgsConstructor // Generates a constructor with all arguments
@NoArgsConstructor // Generates a no-argument constructor
public class AuthenticationResponse {

    private String token;
    private String username;
    private String name;
    private String role;

}