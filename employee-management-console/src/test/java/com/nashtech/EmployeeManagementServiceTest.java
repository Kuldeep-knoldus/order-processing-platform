package com.nashtech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeManagementServiceTest {
    private EmployeeManagementService service;

    @BeforeEach
    void setUp() {
        service = new EmployeeManagementService();
        service.addEmployee(new Employee(101, "Alex", "Engineering", 90000, true));
        service.addEmployee(new Employee(102, "Sam", "Engineering", 125000, true));
        service.addEmployee(new Employee(103, "John", "Finance", 140000, false));
        service.addEmployee(new Employee(104, "Priya", "Engineering", 150000, true));
    }

    @Test
    void addsAndDisplaysEmployees() {
        assertEquals(4, service.getAllEmployees().size());
        assertEquals("Alex", service.findEmployeeById(101).getName());
    }

    @Test
    void searchesByDepartmentIgnoringCase() {
        assertEquals(List.of("Alex", "Sam", "Priya"), service.findByDepartment("engineering")
                .stream().map(Employee::getName).toList());
    }

    @Test
    void findsOnlyActiveEmployeesAboveSalary() {
        assertEquals(List.of("Priya", "Sam"), service.findActiveEmployeesWithSalaryGreaterThan(100000)
                .stream().map(Employee::getName).toList());
    }

    @Test
    void throwsCustomExceptionForUnknownEmployee() {
        assertThrows(EmployeeNotFoundException.class, () -> service.findEmployeeById(999));
    }

    @Test
    void rejectsDuplicateEmployeeIds() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addEmployee(new Employee(101, "Replacement", "Finance", 50000, true)));
    }
}