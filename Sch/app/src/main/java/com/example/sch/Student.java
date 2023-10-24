package com.example.sch;

public class Student {
    private String username;
    private String password;
    private String subject;
    private String rollNumber;

    public Student() {
        // Default constructor required for Firebase
    }

    public Student(String username, String password, String subject, String rollNumber) {
        this.username = username;
        this.password = password;
        this.subject = subject;
        this.rollNumber = rollNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSubject() {
        return subject;
    }

    public String getRollNumber() {
        return rollNumber;
    }
}
