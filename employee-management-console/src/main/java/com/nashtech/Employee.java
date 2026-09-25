package com.nashtech;

import java.util.Objects;

public class Employee {
    private final int employeeId;
    private final String name;
    private final String department;
    private final double salary;
    private boolean active;

    public Employee(int employeeId, String name, String department, double salary, boolean active) {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("Employee ID must be positive.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Employee name is required.");
        }
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("Department is required.");
        }
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }
        this.employeeId = employeeId;
        this.name = name.trim();
        this.department = department.trim();
        this.salary = salary;
        this.active = active;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Employee employee)) {
            return false;
        }
        return employeeId == employee.employeeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }

    @Override
    public String toString() {
        return employeeId + " | " + name + " | " + department + " | " + salary + " | "
                + (active ? "Active" : "Inactive");
    }
}