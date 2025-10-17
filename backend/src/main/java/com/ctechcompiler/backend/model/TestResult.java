package com.ctechcompiler.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_results")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String regNo;
    private String testName;
    private int marks;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TestResult() {}

    public TestResult(String regNo, String testName, int marks, LocalDateTime startTime, LocalDateTime endTime) {
        this.regNo = regNo;
        this.testName = testName;
        this.marks = marks;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}
