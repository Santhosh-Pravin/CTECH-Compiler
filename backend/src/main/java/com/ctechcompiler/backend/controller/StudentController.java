package com.ctechcompiler.backend.controller;

import com.ctechcompiler.backend.model.CodeSnippet; // Import CodeSnippet
import com.ctechcompiler.backend.model.Submission;
import com.ctechcompiler.backend.service.CodeSnippetService; // Import CodeSnippetService
import com.ctechcompiler.backend.service.SubmissionService;
import lombok.RequiredArgsConstructor; // Use Lombok for cleaner constructor
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor // Use this instead of manual constructor
public class StudentController {

    private final SubmissionService submissionService;
    private final CodeSnippetService codeSnippetService; // Add the new service

    // This is the NEW endpoint for students to get their tests
    @GetMapping("/snippets")
    public ResponseEntity<List<CodeSnippet>> getAvailableSnippets() {
        return ResponseEntity.ok(codeSnippetService.getAllSnippets());
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitCode(@RequestBody Map<String, String> body) {
        // This logic remains the same for now
        String username = body.get("username");
        String title = body.get("snippetTitle");
        String code = body.get("submittedCode");

        if (username == null || title == null || code == null)
            return ResponseEntity.badRequest().body("Missing fields");

        Submission s = submissionService.submitCode(username, title, code);
        return ResponseEntity.ok(s);
    }

    @GetMapping("/submissions/{username}")
    public ResponseEntity<List<Submission>> getStudentSubs(@PathVariable String username) {
        return ResponseEntity.ok(submissionService.getSubmissionsForStudent(username));
    }
}
