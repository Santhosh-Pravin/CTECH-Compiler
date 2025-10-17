package com.ctechcompiler.backend.controller;

import com.ctechcompiler.backend.service.CodeSnippetService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * This controller handles all HTTP requests related to Code Snippets,
 * including creating, retrieving, and assigning them.
 */
@RestController
@RequestMapping("/api/snippets") // All endpoints in this class will start with /api/snippets
public class CodeSnippetController {

    private final CodeSnippetService codeSnippetService;

    // Spring Boot automatically injects the service when the controller is created
    public CodeSnippetController(CodeSnippetService codeSnippetService) {
        this.codeSnippetService = codeSnippetService;
    }

    /**
     * API endpoint to assign a code snippet to a student.
     * It listens for POST requests to /api/snippets/assign
     * Example Request Body: { "snippetTitle": "Hello World in Java", "username": "student1" }
     */
    @PostMapping("/assign")
    public ResponseEntity<?> assignSnippet(@RequestBody Map<String, String> payload) {
        try {
            String snippetTitle = payload.get("snippetTitle");
            String username = payload.get("username");

            // Basic validation to ensure the required data was sent
            if (snippetTitle == null || username == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Request must include 'snippetTitle' and 'username'."));
            }

            // Call the service layer to perform the business logic
            codeSnippetService.assignSnippetToUser(snippetTitle, username);

            // Return a successful response to the frontend
            return ResponseEntity.ok().body(Map.of("message", "Snippet '" + snippetTitle + "' assigned to '" + username + "' successfully."));

        } catch (EntityNotFoundException e) {
            // If the service throws an error because the user or snippet wasn't found
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // A general catch-all for any other unexpected errors
            return ResponseEntity.status(500).body(Map.of("error", "An internal server error occurred."));
        }
    }

    // You would also add other endpoints here, for example:
    // @GetMapping to list all snippets
    // @PostMapping to create a new snippet
}


