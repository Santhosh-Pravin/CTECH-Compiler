package com.ctechcompiler.backend.dto;

public class AuthResponse {
    private String username;
    private String role;
    private String name;
    private String token; // <-- ADD THIS FIELD

    public AuthResponse() {}
    public AuthResponse(String username, String role, String name, String token) { // <-- UPDATE CONSTRUCTOR
        this.username = username;
        this.role = role;
        this.name = name;
        this.token = token; // <-- ADD THIS
    }

    // Add getters and setters for all fields
    public String getUsername() { return username; }
    public void setUsername(String u) { username = u; }
    public String getRole() { return role; }
    public void setRole(String r) { role = r; }
    public String getName() { return name; }
    public void setName(String n) { name = n; }
    public String getToken() { return token; } // <-- ADD THIS
    public void setToken(String t) { token = t; } // <-- ADD THIS
}

