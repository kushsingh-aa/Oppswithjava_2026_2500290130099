package com.student.service;

import com.student.dao.StudentDAO;
import com.student.model.Student;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Layer coordinating business logic, Java Collections, and MySQL persistence.
 */
@Service
public class StudentService {

    private final StudentDAO studentDAO;
    // In-memory cache using Java Collections (HashMap & ArrayList)
    private final Map<String, Student> inMemoryMap = new HashMap<>();
    private final List<Student> inMemoryList = new ArrayList<>();

    public StudentService() {
        this(new StudentDAO());
    }

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    /**
     * Requirement 8 & Q3.1: Add a student with duplicate prevention & validation.
     */
    public boolean addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null.");
        }

        // Prevent duplicate student IDs in memory
        if (inMemoryMap.containsKey(student.getStudentId())) {
            System.err.println("Validation Error: Duplicate Student ID '" + student.getStudentId() + "'! (Duplicate prevented)");
            return false;
        }

        // Save to in-memory collection
        inMemoryMap.put(student.getStudentId(), student);
        inMemoryList.add(student);

        // Also persist to MySQL if database is connected
        try {
            if (studentDAO != null && !studentDAO.existsById(student.getStudentId())) {
                studentDAO.saveStudent(student);
            }
        } catch (Exception e) {
            // Log database notice without crashing in-memory execution
            System.out.println("Notice: MySQL persistence optional, cached in memory (" + e.getMessage() + ")");
        }

        return true;
    }

    /**
     * Q3.2: Retrieve all students.
     */
    public List<Student> getAllStudents() {
        try {
            if (studentDAO != null) {
                List<Student> dbStudents = studentDAO.getAllStudents();
                if (!dbStudents.isEmpty()) {
                    return dbStudents;
                }
            }
        } catch (Exception ignored) {
        }
        return Collections.unmodifiableList(inMemoryList);
    }

    /**
     * Q3.3 & Q2.4: Search for a student by ID.
     */
    public Optional<Student> searchById(String studentId) {
        if (studentId == null) return Optional.empty();
        try {
            if (studentDAO != null) {
                Student s = studentDAO.getStudentById(studentId.trim());
                if (s != null) return Optional.of(s);
            }
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(inMemoryMap.get(studentId.trim()));
    }

    /**
     * Q3.4: Update student scores and skills.
     */
    public boolean updateStudent(String studentId, Student updated) {
        if (studentId == null || updated == null) return false;

        boolean updatedInMemory = false;
        Student existing = inMemoryMap.get(studentId);
        if (existing != null) {
            existing.setProgrammingScore(updated.getProgrammingScore());
            existing.setMathematicsScore(updated.getMathematicsScore());
            if (updated.getTechnicalSkills() != null) {
                existing.setTechnicalSkills(updated.getTechnicalSkills());
            }
            updatedInMemory = true;
        }

        try {
            if (studentDAO != null) {
                studentDAO.updateScoresAndSkills(
                        studentId,
                        updated.getProgrammingScore(),
                        updated.getMathematicsScore(),
                        updated.getTechnicalSkills()
                );
            }
        } catch (Exception ignored) {
        }

        return updatedInMemory;
    }

    /**
     * Q3.5: Delete a student record.
     */
    public boolean deleteStudent(String studentId) {
        if (studentId == null) return false;
        boolean removed = false;
        Student s = inMemoryMap.remove(studentId.trim());
        if (s != null) {
            inMemoryList.remove(s);
            removed = true;
        }
        try {
            if (studentDAO != null) {
                studentDAO.deleteStudent(studentId.trim());
            }
        } catch (Exception ignored) {
        }
        return removed;
    }

    /**
     * Q3.6 & Q2.5: Display the merit list sorted by average score descending.
     */
    public List<Student> getMeritList() {
        List<Student> students = new ArrayList<>(getAllStudents());
        students.sort(Comparator.comparingDouble(Student::calculateAverageScore).reversed());
        return students;
    }

    /**
     * Q2.4: Search by Email.
     */
    public Optional<Student> searchByEmail(String email) {
        if (email == null) return Optional.empty();
        return getAllStudents().stream()
                .filter(s -> s.getEmailAddress().equalsIgnoreCase(email.trim()))
                .findFirst();
    }

    /**
     * Q2.4: Search by Course.
     */
    public List<Student> searchByCourse(String course) {
        if (course == null) return Collections.emptyList();
        return getAllStudents().stream()
                .filter(s -> s.getCourseName().equalsIgnoreCase(course.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Q2.6: Identify students sharing the same technical skills.
     */
    public Map<String, List<Student>> identifyStudentsWithSameSkills() {
        List<Student> all = getAllStudents();
        Map<String, List<Student>> skillMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (Student s : all) {
            for (String skill : s.getTechnicalSkills()) {
                skillMap.computeIfAbsent(skill, k -> new ArrayList<>()).add(s);
            }
        }

        Map<String, List<Student>> sharedSkills = new LinkedHashMap<>();
        for (Map.Entry<String, List<Student>> entry : skillMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                sharedSkills.put(entry.getKey(), entry.getValue());
            }
        }
        return sharedSkills;
    }

    // Alias for REST controller
    public Map<String, List<Student>> getStudentsWithSameSkills() {
        return identifyStudentsWithSameSkills();
    }

    /**
     * Q2.7: Students needing programming improvement (<50) but math >= 75.
     */
    public List<Student> getStudentsNeedingProgrammingImprovementWithHighMath() {
        return getAllStudents().stream()
                .filter(s -> s.getProgrammingScore() < 50.0 && s.getMathematicsScore() >= 75.0)
                .collect(Collectors.toList());
    }

    // Alias for REST controller
    public List<Student> getNeedingProgrammingImprovement() {
        return getStudentsNeedingProgrammingImprovementWithHighMath();
    }
}
