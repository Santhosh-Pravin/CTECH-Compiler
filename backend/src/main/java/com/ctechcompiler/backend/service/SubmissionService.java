package com.ctechcompiler.backend.service;

import com.ctechcompiler.backend.model.Submission;
import com.ctechcompiler.backend.repository.SubmissionRepository;
import jakarta.persistence.EntityNotFoundException; // <-- Import this
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SubmissionService {

    private final SubmissionRepository repo;

    public SubmissionService(SubmissionRepository repo) {
        this.repo = repo;
    }

    public Submission submitCode(String studentUsername, String snippetTitle, String submittedCode) {
        Submission s = new Submission();
        s.setStudentUsername(studentUsername);
        s.setSnippetTitle(snippetTitle);
        s.setSubmittedCode(submittedCode);
        s.setCorrect(submittedCode != null && submittedCode.contains("main"));
        return repo.save(s);
    }

    // --- NEW METHOD TO SAVE VIVA RESULTS ---
    public Submission saveVivaResult(Long submissionId, String question, String answer, int score, String feedback) {
        // Find the existing submission by its ID
        Submission submissionToUpdate = repo.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found with ID: " + submissionId));

        // Update the submission object with the new viva details
        submissionToUpdate.setVivaQuestion(question);
        submissionToUpdate.setVivaAnswer(answer);
        submissionToUpdate.setVivaScore(score);
        submissionToUpdate.setVivaFeedback(feedback);

        // Save the updated object back to the database and return it
        return repo.save(submissionToUpdate);
    }

    public List<Submission> getSubmissionsForStudent(String username) {
        return repo.findByStudentUsername(username);
    }

    public List<Submission> getAllSubmissions() {
        return repo.findAll();
    }
}

