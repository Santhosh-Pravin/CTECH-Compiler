package com.ctechcompiler.backend.repository;

import com.ctechcompiler.backend.model.CodeSnippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeSnippetRepository extends JpaRepository<CodeSnippet, Long> {
    // Method to find a code snippet by its unique title
    Optional<CodeSnippet> findByTitle(String title);
}


