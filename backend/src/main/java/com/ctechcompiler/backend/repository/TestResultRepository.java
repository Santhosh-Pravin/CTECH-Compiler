package com.ctechcompiler.backend.repository;

import com.ctechcompiler.backend.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByRegNo(String regNo);
}
