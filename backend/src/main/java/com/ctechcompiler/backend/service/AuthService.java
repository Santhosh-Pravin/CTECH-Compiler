package com.ctechcompiler.backend.service;

import com.ctechcompiler.backend.auth.AuthenticationRequest;
import com.ctechcompiler.backend.auth.AuthenticationResponse;
import com.ctechcompiler.backend.model.User;
import com.ctechcompiler.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    // Inject the AuthenticationManager
    private final AuthenticationManager authenticationManager;

    public User registerStudent(String mentorUsername, String mentorPassword, String studentUsername, String studentPassword, String studentName) {
        // Authenticate the mentor before allowing student registration
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(mentorUsername, mentorPassword)
        );
        User mentor = userRepository.findByUsername(mentorUsername).orElseThrow();
        if (!"MENTOR".equals(mentor.getRole())) {
            throw new RuntimeException("User is not a mentor and cannot register students.");
        }

        User student = new User();
        student.setUsername(studentUsername);
        student.setName(studentName);
        student.setPassword(passwordEncoder.encode(studentPassword));
        student.setRole("STUDENT");
        return userRepository.save(student);
    }

    public User registerMentor(String username, String password, String name) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username '" + username + "' is already taken.");
        }
        User mentor = new User();
        mentor.setUsername(username);
        mentor.setName(name);
        mentor.setPassword(passwordEncoder.encode(password));
        mentor.setRole("MENTOR");
        return userRepository.save(mentor);
    }

    /**
     * Authenticates a user and returns a token response.
     * This is the new, correct way to handle login.
     */
    public AuthenticationResponse login(AuthenticationRequest request) {
        // This line will throw an exception if credentials are bad
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // If authentication was successful, find the user and generate a token
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database"));

        var jwtToken = jwtService.generateToken(user);

        // Build and return the successful response
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}

