package com.ctechcompiler.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentUsername;
    private String snippetTitle;

    @Column(length = 8000)
    private String submittedCode;

    // --- THIS IS THE NEW FIELD ---
    private boolean correct;

    // --- NEW FIELDS FOR VIVA RESULTS ---
    @Column(length = 1000)
    private String vivaQuestion;

    @Column(length = 2000)
    private String vivaAnswer;

    private int vivaScore; // e.g., out of 10

    @Column(length = 2000)
    private String vivaFeedback; // AI-generated feedback
}


