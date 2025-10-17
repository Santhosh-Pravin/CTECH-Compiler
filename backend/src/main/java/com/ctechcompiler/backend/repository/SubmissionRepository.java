package com.ctechcompiler.backend.repository;

import com.ctechcompiler.backend.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudentUsername(String studentUsername);
}
