package com.student.controller;

import com.student.model.Student;
import com.student.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Question 3: REST Controller implementing endpoints for:
 * 1. Add a student.
 * 2. Retrieve all students.
 * 3. Search for a student by ID.
 * 4. Update student scores and skills.
 * 5. Delete a student record.
 * 6. Display the merit list.
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // 1. Add a student (POST /api/students)
    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        try {
            boolean added = studentService.addStudent(student);
            if (added) {
                return new ResponseEntity<>(student, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Error: Student with ID '" + student.getStudentId() + "' already exists! (Duplicate prevented)", HttpStatus.CONFLICT);
            }
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Validation Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 2. Retrieve all students (GET /api/students)
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // 3. Search for a student by ID (GET /api/students/{id})
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") String id) {
        Optional<Student> student = studentService.searchById(id);
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        }
        return new ResponseEntity<>("Student with ID '" + id + "' not found.", HttpStatus.NOT_FOUND);
    }

    // 4. Update student scores and skills (PUT /api/students/{id})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudentScoresAndSkills(@PathVariable("id") String id, @RequestBody Student updatedData) {
        try {
            boolean updated = studentService.updateStudent(id, updatedData);
            if (updated) {
                return ResponseEntity.ok("Student ID '" + id + "' scores and skills updated successfully.");
            } else {
                return new ResponseEntity<>("Student with ID '" + id + "' not found.", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Validation Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 5. Delete a student record (DELETE /api/students/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") String id) {
        boolean deleted = studentService.deleteStudent(id);
        if (deleted) {
            return ResponseEntity.ok("Student record with ID '" + id + "' deleted successfully.");
        }
        return new ResponseEntity<>("Student with ID '" + id + "' not found.", HttpStatus.NOT_FOUND);
    }

    // 6. Display the merit list (GET /api/students/merit-list)
    @GetMapping("/merit-list")
    public ResponseEntity<List<Student>> getMeritList() {
        return ResponseEntity.ok(studentService.getMeritList());
    }

    // Extra: Search by Email (GET /api/students/search/email?email=...)
    @GetMapping("/search/email")
    public ResponseEntity<?> searchByEmail(@RequestParam("email") String email) {
        return studentService.searchByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Extra: Search by Course (GET /api/students/search/course?course=...)
    @GetMapping("/search/course")
    public ResponseEntity<List<Student>> searchByCourse(@RequestParam("course") String course) {
        return ResponseEntity.ok(studentService.searchByCourse(course));
    }

    // Extra: Group students with same skills (GET /api/students/skills/shared)
    @GetMapping("/skills/shared")
    public ResponseEntity<Map<String, List<Student>>> getSharedSkills() {
        return ResponseEntity.ok(studentService.getStudentsWithSameSkills());
    }

    // Extra: Students needing programming improvement (<50) with Math >= 75
    @GetMapping("/needing-improvement")
    public ResponseEntity<List<Student>> getNeedingImprovement() {
        return ResponseEntity.ok(studentService.getNeedingProgrammingImprovement());
    }
}
