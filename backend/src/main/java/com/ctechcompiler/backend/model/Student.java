package com.ctechcompiler.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String regNo;
    private String name;
    private String password;
    private String course;
    private String email;

    public Student() {}

    public Student(String regNo, String name, String password, String course, String email) {
        this.regNo = regNo;
        this.name = name;
        this.password = password;
        this.course = course;
        this.email = email;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
