package com.student.dao;

import com.student.model.Student;
import com.student.util.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for interacting with MySQL database using JDBC.
 */
@Repository
public class StudentDAO {

    /**
     * Check if student ID exists in database.
     */
    public boolean existsById(String studentId) {
        String query = "SELECT COUNT(*) FROM students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error checking student existence: " + e.getMessage());
        }
        return false;
    }

    /**
     * 1. Inserts a student and their skills into MySQL tables.
     */
    public boolean saveStudent(Student student) {
        String studentSql = "INSERT INTO students (student_id, student_name, email_address, course_name, programming_score, mathematics_score) VALUES (?, ?, ?, ?, ?, ?)";
        String skillSql = "INSERT INTO student_skills (student_id, skill_name) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Begin transaction

            try (PreparedStatement pstmtStudent = conn.prepareStatement(studentSql)) {
                pstmtStudent.setString(1, student.getStudentId());
                pstmtStudent.setString(2, student.getStudentName());
                pstmtStudent.setString(3, student.getEmailAddress());
                pstmtStudent.setString(4, student.getCourseName());
                pstmtStudent.setDouble(5, student.getProgrammingScore());
                pstmtStudent.setDouble(6, student.getMathematicsScore());
                pstmtStudent.executeUpdate();
            }

            if (student.getTechnicalSkills() != null && !student.getTechnicalSkills().isEmpty()) {
                try (PreparedStatement pstmtSkill = conn.prepareStatement(skillSql)) {
                    for (String skill : student.getTechnicalSkills()) {
                        pstmtSkill.setString(1, student.getStudentId());
                        pstmtSkill.setString(2, skill);
                        pstmtSkill.addBatch();
                    }
                    pstmtSkill.executeBatch();
                }
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error saving student: " + e.getMessage());
            return false;
        }
    }

    /**
     * 2. Retrieves all students along with their technical skills.
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT student_id, student_name, email_address, course_name, programming_score, mathematics_score FROM students";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = rs.getString("student_id");
                Student student = new Student(
                        id,
                        rs.getString("student_name"),
                        rs.getString("email_address"),
                        rs.getString("course_name"),
                        rs.getDouble("programming_score"),
                        rs.getDouble("mathematics_score")
                );
                student.setTechnicalSkills(getSkillsForStudent(conn, id));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Database Error fetching students: " + e.getMessage());
        }
        return students;
    }

    /**
     * 3. Search for a student by ID.
     */
    public Student getStudentById(String studentId) {
        String query = "SELECT student_id, student_name, email_address, course_name, programming_score, mathematics_score FROM students WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student(
                            rs.getString("student_id"),
                            rs.getString("student_name"),
                            rs.getString("email_address"),
                            rs.getString("course_name"),
                            rs.getDouble("programming_score"),
                            rs.getDouble("mathematics_score")
                    );
                    student.setTechnicalSkills(getSkillsForStudent(conn, studentId));
                    return student;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error fetching student by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * 4. Update student scores and skills.
     */
    public boolean updateScoresAndSkills(String studentId, double programmingScore, double mathematicsScore, List<String> skills) {
        String updateSql = "UPDATE students SET programming_score = ?, mathematics_score = ? WHERE student_id = ?";
        String deleteSkillsSql = "DELETE FROM student_skills WHERE student_id = ?";
        String insertSkillSql = "INSERT INTO student_skills (student_id, skill_name) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setDouble(1, programmingScore);
                pstmt.setDouble(2, mathematicsScore);
                pstmt.setString(3, studentId);
                int rows = pstmt.executeUpdate();
                if (rows == 0) {
                    conn.rollback();
                    return false;
                }
            }

            if (skills != null) {
                try (PreparedStatement delStmt = conn.prepareStatement(deleteSkillsSql)) {
                    delStmt.setString(1, studentId);
                    delStmt.executeUpdate();
                }

                try (PreparedStatement insStmt = conn.prepareStatement(insertSkillSql)) {
                    for (String skill : skills) {
                        insStmt.setString(1, studentId);
                        insStmt.setString(2, skill);
                        insStmt.addBatch();
                    }
                    insStmt.executeBatch();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error updating student: " + e.getMessage());
            return false;
        }
    }

    /**
     * 5. Deletes a student by ID (skills deleted automatically via CASCADE).
     */
    public boolean deleteStudent(String studentId) {
        String query = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database Error deleting student: " + e.getMessage());
            return false;
        }
    }

    /**
     * 6. Display merit list sorted by average score descending.
     */
    public List<Student> getMeritList() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT student_id, student_name, email_address, course_name, programming_score, mathematics_score " +
                       "FROM students ORDER BY (programming_score + mathematics_score)/2.0 DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = rs.getString("student_id");
                Student student = new Student(
                        id,
                        rs.getString("student_name"),
                        rs.getString("email_address"),
                        rs.getString("course_name"),
                        rs.getDouble("programming_score"),
                        rs.getDouble("mathematics_score")
                );
                student.setTechnicalSkills(getSkillsForStudent(conn, id));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Database Error fetching merit list: " + e.getMessage());
        }
        return students;
    }

    /**
     * Helper method to fetch skills for a student.
     */
    private List<String> getSkillsForStudent(Connection conn, String studentId) throws SQLException {
        List<String> skills = new ArrayList<>();
        String skillQuery = "SELECT skill_name FROM student_skills WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(skillQuery)) {
            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    skills.add(rs.getString("skill_name"));
                }
            }
        }
        return skills;
    }
}
