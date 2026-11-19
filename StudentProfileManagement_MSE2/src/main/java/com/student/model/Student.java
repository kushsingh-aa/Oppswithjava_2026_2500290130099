package com.student.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student {

    private String studentId;
    private String studentName;
    private String emailAddress;
    private String courseName;
    private double programmingScore;
    private double mathematicsScore;
    private List<String> technicalSkills;

    // 1. Default Constructor
    public Student() {
        this.technicalSkills = new ArrayList<>();
    }

    // 2. Parameterized Constructor (without skills list, initializes empty)
    public Student(String studentId, String studentName, String emailAddress, String courseName,
            double programmingScore, double mathematicsScore) {
        setStudentId(studentId);
        setStudentName(studentName);
        setEmailAddress(emailAddress);
        setCourseName(courseName);
        setProgrammingScore(programmingScore);
        setMathematicsScore(mathematicsScore);
        this.technicalSkills = new ArrayList<>();
    }

    // 3. Fully Parameterized Constructor
    public Student(String studentId, String studentName, String emailAddress, String courseName,
            double programmingScore, double mathematicsScore, List<String> technicalSkills) {
        setStudentId(studentId);
        setStudentName(studentName);
        setEmailAddress(emailAddress);
        setCourseName(courseName);
        setProgrammingScore(programmingScore);
        setMathematicsScore(mathematicsScore);
        setTechnicalSkills(technicalSkills);
    }

    // --- Getters and Setters with Encapsulation & Validation ---

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty.");
        }
        this.studentId = studentId.trim();
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        if (studentName == null || studentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty.");
        }
        this.studentName = studentName.trim();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        if (emailAddress == null || !emailAddress.contains("@")) {
            throw new IllegalArgumentException("Invalid email address format.");
        }
        this.emailAddress = emailAddress.trim();
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty.");
        }
        this.courseName = courseName.trim();
    }

    public double getProgrammingScore() {
        return programmingScore;
    }

    public void setProgrammingScore(double programmingScore) {
        if (programmingScore < 0.0 || programmingScore > 100.0) {
            throw new IllegalArgumentException(
                    "Programming score must be between 0 and 100. Given: " + programmingScore);
        }
        this.programmingScore = programmingScore;
    }

    public double getMathematicsScore() {
        return mathematicsScore;
    }

    public void setMathematicsScore(double mathematicsScore) {
        if (mathematicsScore < 0.0 || mathematicsScore > 100.0) {
            throw new IllegalArgumentException(
                    "Mathematics score must be between 0 and 100. Given: " + mathematicsScore);
        }
        this.mathematicsScore = mathematicsScore;
    }

    public List<String> getTechnicalSkills() {
        // Return a defensive copy to protect internal state encapsulation
        return new ArrayList<>(technicalSkills);
    }

    public void setTechnicalSkills(List<String> technicalSkills) {
        if (technicalSkills == null) {
            this.technicalSkills = new ArrayList<>();
        } else {
            // Defensive copy and sanitize
            this.technicalSkills = new ArrayList<>();
            for (String skill : technicalSkills) {
                if (skill != null && !skill.trim().isEmpty()) {
                    this.technicalSkills.add(skill.trim());
                }
            }
        }
    }

    // --- Domain / Business Methods ---

    /**
     * Requirement 1: Calculate the student's average score.
     */
    public double calculateAverageScore() {
        return Math.round(((this.programmingScore + this.mathematicsScore) / 2.0) * 100.0) / 100.0;
    }

    public double getAverageScore() {
        return calculateAverageScore();
    }

    /**
     * Requirement 2: Assign performance category based on average score.
     * - 85 or above: Excellent
     * - 70 to 84.99: Good
     * - 50 to 69.99: Satisfactory
     * - Below 50: Needs Improvement
     */
    public String getPerformanceCategory() {
        double avg = calculateAverageScore();
        if (avg >= 85.0) {
            return "Excellent";
        } else if (avg >= 70.0) {
            return "Good";
        } else if (avg >= 50.0) {
            return "Satisfactory";
        } else {
            return "Needs Improvement";
        }
    }

    /**
     * Requirement 3: Identify the stronger subject for the student.
     */
    public String getStrongerSubject() {
        if (programmingScore > mathematicsScore) {
            return "Programming";
        } else if (mathematicsScore > programmingScore) {
            return "Mathematics";
        } else {
            return "Equal Performance";
        }
    }

    /**
     * Add a technical skill to the list.
     */
    public void addSkill(String skill) {
        if (skill != null && !skill.trim().isEmpty()) {
            String trimmed = skill.trim();
            if (!this.technicalSkills.contains(trimmed)) {
                this.technicalSkills.add(trimmed);
            }
        }
    }

    /**
     * Remove a technical skill from the list.
     */
    public void removeSkill(String skill) {
        if (skill != null) {
            this.technicalSkills.remove(skill.trim());
        }
    }

    /**
     * Check if the student possesses a given skill (case-insensitive).
     */
    public boolean hasSkill(String skill) {
        if (skill == null)
            return false;
        return this.technicalSkills.stream()
                .anyMatch(s -> s.equalsIgnoreCase(skill.trim()));
    }

    // --- Appropriate Object Methods (toString, equals, hashCode) ---

    @Override
    public String toString() {
        return String.format(
                "Student [ID=%s, Name=%s, Email=%s, Course=%s, Programming=%.2f, Math=%.2f, Avg=%.2f, Category=%s, StrongerSubject=%s, Skills=%s]",
                studentId, studentName, emailAddress, courseName,
                programmingScore, mathematicsScore, calculateAverageScore(),
                getPerformanceCategory(), getStrongerSubject(), technicalSkills);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Student))
            return false;
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
