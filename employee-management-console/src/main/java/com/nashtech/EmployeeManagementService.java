package com.nashtech;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeManagementService {
    private final Map<Integer, Employee> employees = new LinkedHashMap<>();

    public void addEmployee(Employee employee) {
        if (employees.containsKey(employee.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists: " + employee.getEmployeeId());
        }
        employees.put(employee.getEmployeeId(), employee);
    }

    public List<Employee> getAllEmployees() {
        return List.copyOf(employees.values());
    }

    public Employee findEmployeeById(int employeeId) {
        Employee employee = employees.get(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId);
        }
        return employee;
    }

    public List<Employee> findByDepartment(String department) {
        return employees.values().stream()
                .filter(employee -> employee.getDepartment().equalsIgnoreCase(department.trim()))
                .toList();
    }

    public List<Employee> findActiveEmployeesWithSalaryGreaterThan(double salary) {
        return employees.values().stream()
                .filter(Employee::isActive)
                .filter(employee -> employee.getSalary() > salary)
                .sorted(Comparator.comparing(Employee::getName))
                .collect(Collectors.toList());
    }

    public List<Employee> getEmployeesSnapshot() {
        return new ArrayList<>(employees.values());
    }
}