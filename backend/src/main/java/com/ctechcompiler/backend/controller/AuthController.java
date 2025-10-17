package com.ctechcompiler.backend.controller;

// --- Notice the corrected import statements ---
import com.ctechcompiler.backend.auth.AuthenticationRequest;
import com.ctechcompiler.backend.auth.AuthenticationResponse;
import com.ctechcompiler.backend.dto.RegisterStudentRequest;
import com.ctechcompiler.backend.model.User;
import com.ctechcompiler.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    // The method now uses the standard AuthenticationRequest class
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/register-student")
    public ResponseEntity<?> registerStudent(@RequestBody RegisterStudentRequest request) {
        try {
            User student = authService.registerStudent(
                    request.mentorUsername,
                    request.mentorPassword,
                    request.studentUsername,
                    request.studentPassword,
                    request.studentName
            );
            return ResponseEntity.ok(Map.of("message", "Student " + student.getUsername() + " registered successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/register-mentor")
    // This method also uses the standard AuthenticationRequest class
    public ResponseEntity<?> registerMentor(@RequestBody AuthenticationRequest request) {
        try {
            // The request object now correctly provides all needed methods thanks to Lombok
            User mentor = authService.registerMentor(request.getUsername(), request.getPassword(), request.getName());
            return ResponseEntity.ok(Map.of("message", "Mentor " + mentor.getUsername() + " registered successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
