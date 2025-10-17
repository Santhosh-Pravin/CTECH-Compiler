package com.ctechcompiler.backend.service;

import com.ctechcompiler.backend.model.TestResult;
import com.ctechcompiler.backend.repository.TestResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    private final TestResultRepository repository;

    public TestService(TestResultRepository repository) {
        this.repository = repository;
    }

    public TestResult saveResult(TestResult result) {
        return repository.save(result);
    }

    public List<TestResult> getResultsByRegNo(String regNo) {
        return repository.findByRegNo(regNo);
    }
}
