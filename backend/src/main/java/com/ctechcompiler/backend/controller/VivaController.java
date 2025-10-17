package com.ctechcompiler.backend.controller;

import com.ctechcompiler.backend.service.SubmissionService;
import com.ctechcompiler.backend.service.VivaService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/viva")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class VivaController {

    private final VivaService vivaService;
    private final SubmissionService submissionService;

    @PostMapping("/generate-question")
    public ResponseEntity<String> generateQuestion(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        if (code == null || code.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Code cannot be empty.");
        }
        try {
            String question = vivaService.generateVivaQuestionFromCode(code);
            return ResponseEntity.ok(question);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate viva question.");
        }
    }

    // THIS IS THE NEW ENDPOINT FOR EVALUATION
    @PostMapping("/evaluate-answer")
    public ResponseEntity<?> evaluateAnswer(@RequestBody Map<String, Object> body) {
        try {
            Long submissionId = ((Number) body.get("submissionId")).longValue();
            String code = (String) body.get("code");
            String question = (String) body.get("question");
            String answer = (String) body.get("answer");

            JSONObject evaluation = vivaService.evaluateVivaAnswer(code, question, answer);
            int score = evaluation.getInt("score");
            String feedback = evaluation.getString("feedback");

            submissionService.saveVivaResult(submissionId, question, answer, score, feedback);

            return ResponseEntity.ok(Map.of("score", score, "feedback", feedback));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to evaluate viva: " + e.getMessage());
        }
    }
}