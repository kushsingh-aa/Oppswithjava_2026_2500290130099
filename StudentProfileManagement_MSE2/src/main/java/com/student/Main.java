package com.student;

import com.student.dao.StudentDAO;
import com.student.model.Student;
import com.student.service.StudentService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Main Driver Class demonstrating:
 * - Question 1: Student class encapsulation, constructors, getters/setters, object methods.
 * - Question 2: Business rules, Java collections (List, Map, Set, Streams), and requirement implementations.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("   STUDENT PROFILE AND SKILL DEVELOPMENT MANAGEMENT SYSTEM (MSE-II)");
        System.out.println("================================================================================\n");

        StudentService service = new StudentService(new StudentDAO());

        // -------------------------------------------------------------------------
        // 1. DEMONSTRATING QUESTION 1: Object Creation, Encapsulation & Skills
        // -------------------------------------------------------------------------
        System.out.println(">>> 1. CREATING STUDENT OBJECTS WITH ENCAPSULATION & TECHNICAL SKILLS:");

        Student s1 = new Student("STU101", "Aarav Sharma", "aarav.sharma@example.com", "CSE", 92.5, 88.0,
                Arrays.asList("Java", "Spring Boot", "MySQL"));

        Student s2 = new Student("STU102", "Diya Patel", "diya.patel@example.com", "CSEAI", 78.0, 82.0,
                Arrays.asList("Python", "Java", "Machine Learning"));

        Student s3 = new Student("STU103", "Rohan Verma", "rohan.verma@example.com", "IT", 42.0, 78.5,
                Arrays.asList("Java", "MySQL", "C++"));

        Student s4 = new Student("STU104", "Ananya Gupta", "ananya.gupta@example.com", "CSEDS", 45.0, 48.0,
                Arrays.asList("Python", "HTML/CSS"));

        Student s5 = new Student("STU105", "Vikram Singh", "vikram.singh@example.com", "CSIT", 88.0, 88.0,
                Arrays.asList("Java", "Spring Boot", "AWS"));

        Student s6 = new Student("STU106", "Sneha Nair", "sneha.nair@example.com", "CSE", 38.0, 80.0,
                Arrays.asList("Python", "Data Structures"));

        // Add students to service
        service.addStudent(s1);
        service.addStudent(s2);
        service.addStudent(s3);
        service.addStudent(s4);
        service.addStudent(s5);
        service.addStudent(s6);

        // Display each student using toString()
        for (Student s : service.getAllStudents()) {
            System.out.println("  * " + s);
        }
        System.out.println();

        // -------------------------------------------------------------------------
        // 2. DEMONSTRATING VALIDATION (Requirement 8): Duplicate ID & Score Bounds
        // -------------------------------------------------------------------------
        System.out.println(">>> 2. DEMONSTRATING SCORE VALIDATION & DUPLICATE PREVENTION (Requirement 8):");
        
        // Test Duplicate ID Prevention
        System.out.println("Attempting to add duplicate Student ID 'STU101':");
        Student duplicateStudent = new Student("STU101", "Imposter", "imposter@example.com", "CSE", 75.0, 80.0);
        boolean added = service.addStudent(duplicateStudent);
        System.out.println("Duplicate added successfully? " + added);

        // Test Score Bounds Validation (0 - 100)
        try {
            System.out.println("Attempting to set programming score to 105.0:");
            s1.setProgrammingScore(105.0);
        } catch (IllegalArgumentException ex) {
            System.out.println("Caught Expected Validation Exception: " + ex.getMessage());
        }

        try {
            System.out.println("Attempting to set mathematics score to -10.0:");
            s1.setMathematicsScore(-10.0);
        } catch (IllegalArgumentException ex) {
            System.out.println("Caught Expected Validation Exception: " + ex.getMessage());
        }
        System.out.println();

        // -------------------------------------------------------------------------
        // 3. REQUIREMENTS 1, 2, 3: Average, Performance Category & Stronger Subject
        // -------------------------------------------------------------------------
        System.out.println(">>> 3. STUDENT ACADEMIC METRICS (Average, Category, Stronger Subject):");
        System.out.printf("%-10s %-16s %-12s %-12s %-10s %-18s %-15s%n",
                "ID", "Name", "Prog Score", "Math Score", "Average", "Category", "Stronger Subject");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Student s : service.getAllStudents()) {
            System.out.printf("%-10s %-16s %-12.2f %-12.2f %-10.2f %-18s %-15s%n",
                    s.getStudentId(),
                    s.getStudentName(),
                    s.getProgrammingScore(),
                    s.getMathematicsScore(),
                    s.calculateAverageScore(),
                    s.getPerformanceCategory(),
                    s.getStrongerSubject());
        }
        System.out.println();

        // -------------------------------------------------------------------------
        // 4. REQUIREMENT 4: Search for Students by ID, Email, or Course
        // -------------------------------------------------------------------------
        System.out.println(">>> 4. SEARCH FUNCTIONALITY (Requirement 4):");
        System.out.println("a) Search by ID 'STU102':");
        service.searchById("STU102").ifPresent(s -> System.out.println("   Found: " + s.getStudentName() + " (" + s.getEmailAddress() + ")"));

        System.out.println("b) Search by Email 'rohan.verma@example.com':");
        service.searchByEmail("rohan.verma@example.com").ifPresent(s -> System.out.println("   Found: " + s.getStudentName() + " in " + s.getCourseName()));

        System.out.println("c) Search by Course 'CSE':");
        List<Student> cseStudents = service.searchByCourse("CSE");
        cseStudents.forEach(s -> System.out.println("   Found: " + s.getStudentName() + " (ID: " + s.getStudentId() + ")"));
        System.out.println();

        // -------------------------------------------------------------------------
        // 5. REQUIREMENT 5: Merit List (Sorted by Average Score in Descending Order)
        // -------------------------------------------------------------------------
        System.out.println(">>> 5. MERIT LIST (Sorted by Average Score in Descending Order - Requirement 5):");
        List<Student> meritList = service.getMeritList();
        int rank = 1;
        System.out.printf("%-6s %-10s %-16s %-10s %-10s %-18s%n",
                "Rank", "ID", "Name", "Course", "Average", "Performance Category");
        System.out.println("---------------------------------------------------------------------------");
        for (Student s : meritList) {
            System.out.printf("%-6d %-10s %-16s %-10s %-10.2f %-18s%n",
                    rank++,
                    s.getStudentId(),
                    s.getStudentName(),
                    s.getCourseName(),
                    s.calculateAverageScore(),
                    s.getPerformanceCategory());
        }
        System.out.println();

        // -------------------------------------------------------------------------
        // 6. REQUIREMENT 6: Identify Students Who Have The Same Technical Skills
        // -------------------------------------------------------------------------
        System.out.println(">>> 6. STUDENTS SHARING THE SAME TECHNICAL SKILLS (Requirement 6):");
        Map<String, List<Student>> sharedSkills = service.identifyStudentsWithSameSkills();
        for (Map.Entry<String, List<Student>> entry : sharedSkills.entrySet()) {
            System.out.println("  Skill: [" + entry.getKey() + "] (Total: " + entry.getValue().size() + " students)");
            for (Student s : entry.getValue()) {
                System.out.println("    - " + s.getStudentName() + " (" + s.getStudentId() + " - " + s.getCourseName() + ")");
            }
        }
        System.out.println();

        // -------------------------------------------------------------------------
        // 7. REQUIREMENT 7: Programming < 50 and Mathematics >= 75
        // -------------------------------------------------------------------------
        System.out.println(">>> 7. STUDENTS NEEDING IMPROVEMENT IN PROGRAMMING (<50) BUT MATH >= 75 (Requirement 7):");
        List<Student> filteredStudents = service.getStudentsNeedingProgrammingImprovementWithHighMath();
        for (Student s : filteredStudents) {
            System.out.printf("  * %s (%s) - Programming: %.2f, Mathematics: %.2f, Avg: %.2f, Skills: %s%n",
                    s.getStudentName(), s.getStudentId(),
                    s.getProgrammingScore(), s.getMathematicsScore(),
                    s.calculateAverageScore(), s.getTechnicalSkills());
        }
        System.out.println();

        // -------------------------------------------------------------------------
        // 8. APPROPRIATE OBJECT METHODS DEMONSTRATION (equals and hashCode)
        // -------------------------------------------------------------------------
        System.out.println(">>> 8. OBJECT METHODS DEMONSTRATION (equals & hashCode):");
        Student test1 = new Student("STU101", "Aarav Sharma", "aarav@test.com", "CSE", 90.0, 90.0);
        Student test2 = new Student("STU101", "Aarav Duplicate", "aarav2@test.com", "CSE", 80.0, 80.0);
        System.out.println("test1.equals(test2) (Same ID 'STU101'): " + test1.equals(test2));
        System.out.println("test1.hashCode() == test2.hashCode(): " + (test1.hashCode() == test2.hashCode()));
        System.out.println("\nAll Question 1 and 2 requirements demonstrated successfully!");
    }
}
