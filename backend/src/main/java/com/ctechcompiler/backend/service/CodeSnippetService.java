package com.ctechcompiler.backend.service;

import com.ctechcompiler.backend.model.CodeSnippet;
import com.ctechcompiler.backend.model.User;
import com.ctechcompiler.backend.repository.CodeSnippetRepository;
import com.ctechcompiler.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CodeSnippetService { // <-- Class definition starts here

    private final CodeSnippetRepository codeSnippetRepository;
    private final UserRepository userRepository;

    public CodeSnippetService(CodeSnippetRepository codeSnippetRepository, UserRepository userRepository) {
        this.codeSnippetRepository = codeSnippetRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void assignSnippetToUser(String snippetTitle, String username) {
        CodeSnippet snippet = codeSnippetRepository.findByTitle(snippetTitle)
                .orElseThrow(() -> new EntityNotFoundException("CodeSnippet not found with title: " + snippetTitle));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        user.getAssignedSnippets().add(snippet);
    }

    public List<CodeSnippet> getAllSnippets() {
        return codeSnippetRepository.findAll();
    }

    // --- NEW METHOD FOR MENTORS TO CREATE SNIPPETS ---
    public CodeSnippet createSnippet(String title, String code) {
        if (codeSnippetRepository.findByTitle(title).isPresent()) {
            throw new RuntimeException("A snippet with the title '" + title + "' already exists.");
        }
        CodeSnippet newSnippet = new CodeSnippet();
        newSnippet.setTitle(title);
        newSnippet.setCode(code);
        return codeSnippetRepository.save(newSnippet);
    }

} // <-- IMPORTANT: This is the final closing brace for the class. Make sure all methods are above this line.

