package com.ctechcompiler.backend.controller;

import com.ctechcompiler.backend.model.CodeSnippet;
import com.ctechcompiler.backend.service.CodeSnippetService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/mentor")
@RequiredArgsConstructor
public class MentorController {

    private final CodeSnippetService codeSnippetService;

    @PostMapping("/snippets/create")
    public ResponseEntity<?> createSnippet(@RequestBody Map<String, String> payload) {
        try {
            String title = payload.get("title");
            String code = payload.get("code");

            if (title == null || title.isBlank() || code == null || code.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Request must include a non-empty 'title' and 'code'."));
            }
            CodeSnippet createdSnippet = codeSnippetService.createSnippet(title, code);
            return ResponseEntity.ok(createdSnippet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected server error occurred. Please check the backend logs."));
        }
    }

    // --- THIS IS THE CORRECTED METHOD ---
    @PostMapping("/snippets/assign")
    public ResponseEntity<?> assignSnippet(@RequestBody Map<String, String> payload) {
        try {
            String snippetTitle = payload.get("snippetTitle");
            String username = payload.get("username");

            if (snippetTitle == null || username == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Request must include 'snippetTitle' and 'username'."));
            }
            codeSnippetService.assignSnippetToUser(snippetTitle, username);
            return ResponseEntity.ok().body(Map.of("message", "Snippet '" + snippetTitle + "' assigned to '" + username + "' successfully."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An internal server error occurred."));
        }
    }
}