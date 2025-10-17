package com.ctechcompiler.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class CodeSnippet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(length = 8000)
    private String code;

    // --- MAPPED BY THE FIELD IN THE USER CLASS ---
    @ManyToMany(mappedBy = "assignedSnippets", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> assignedToUsers = new HashSet<>();
}
