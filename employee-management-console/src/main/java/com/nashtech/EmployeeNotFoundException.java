package com.nashtech;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(int employeeId) {
        super("Employee with ID " + employeeId + " was not found.");
    }
}