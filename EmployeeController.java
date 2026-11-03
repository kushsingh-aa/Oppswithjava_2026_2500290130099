package com.employeeManagement.employee_API;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final List<Employee> employees = new ArrayList<>();

    public EmployeeController() {
        // Pre-populating default employees
        employees.add(new Employee(1, "Aarav Sharma", "IT", 45000));
        employees.add(new Employee(2, "Meera Singh", "HR", 42000));
    }

    // (b) Add a new employee (@PostMapping and @RequestBody)
    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
        if (findEmployee(employee.getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Employee ID already exists.");
        }
        employees.add(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    // (c) Retrieve all employees (@GetMapping)
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employees;
    }

    // (c) Retrieve a specific employee by ID (@GetMapping)
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable int id) {
        Optional<Employee> result = findEmployee(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
    }

    // (d) Update employee details (@PutMapping)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable int id, @RequestBody Employee updatedEmployee) {
        Optional<Employee> result = findEmployee(id);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
        }
        Employee existing = result.get();
        existing.setName(updatedEmployee.getName());
        existing.setDepartment(updatedEmployee.getDepartment());
        existing.setSalary(updatedEmployee.getSalary());
        return ResponseEntity.ok(existing);
    }

    // (e) Delete employee by ID (@DeleteMapping)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id) {
        Optional<Employee> result = findEmployee(id);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
        }
        employees.remove(result.get());
        return ResponseEntity.ok("Employee deleted successfully.");
    }

    private Optional<Employee> findEmployee(int id) {
        return employees.stream().filter(e -> e.getId() == id).findFirst();
    }
}