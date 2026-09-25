package com.nashtech;

import java.util.List;
import java.util.Scanner;

public class EmployeeManagementApplication {
    private final EmployeeManagementService service;

    public EmployeeManagementApplication(EmployeeManagementService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        EmployeeManagementApplication application = new EmployeeManagementApplication(
                createSampleService());
        application.run();
    }

    private static EmployeeManagementService createSampleService() {
        EmployeeManagementService service = new EmployeeManagementService();
        service.addEmployee(new Employee(101, "Alex", "Engineering", 90000, true));
        service.addEmployee(new Employee(102, "Sam", "Engineering", 125000, true));
        service.addEmployee(new Employee(103, "John", "Finance", 140000, false));
        service.addEmployee(new Employee(104, "Priya", "Engineering", 150000, true));
        return service;
    }

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = scanner.nextLine().trim();
                try {
                    running = handleChoice(choice, scanner);
                } catch (IllegalArgumentException exception) {
                    System.out.println("Error: " + exception.getMessage());
                }
            }
        }
    }

    boolean handleChoice(String choice, Scanner scanner) {
        return switch (choice) {
            case "1" -> {
                addEmployee(scanner);
                yield true;
            }
            case "2" -> {
                printEmployees(service.getAllEmployees());
                yield true;
            }
            case "3" -> {
                int employeeId = readInt(scanner, "Employee ID: ");
                System.out.println(service.findEmployeeById(employeeId));
                yield true;
            }
            case "4" -> {
                System.out.print("Department: ");
                printEmployees(service.findByDepartment(scanner.nextLine()));
                yield true;
            }
            case "5" -> {
                double salary = readDouble(scanner, "Minimum salary: ");
                printEmployees(service.findActiveEmployeesWithSalaryGreaterThan(salary));
                yield true;
            }
            case "6" -> {
                System.out.println("Goodbye.");
                yield false;
            }
            default -> throw new IllegalArgumentException("Choose an option from 1 to 6.");
        };
    }

    private void addEmployee(Scanner scanner) {
        int employeeId = readInt(scanner, "Employee ID: ");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Department: ");
        String department = scanner.nextLine();
        double salary = readDouble(scanner, "Salary: ");
        boolean active = readBoolean(scanner, "Active (true/false): ");
        service.addEmployee(new Employee(employeeId, name, department, salary, active));
        System.out.println("Employee added.");
    }

    private int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private double readDouble(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(scanner.nextLine().trim());
    }

    private boolean readBoolean(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();
        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            throw new IllegalArgumentException("Active status must be true or false.");
        }
        return Boolean.parseBoolean(value);
    }

    private void printMenu() {
        System.out.println("\nEmployee Management Console");
        System.out.println("1. Add employee");
        System.out.println("2. Display all employees");
        System.out.println("3. Search by employee ID");
        System.out.println("4. Display by department");
        System.out.println("5. Display active employees above salary");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private void printEmployees(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        employees.forEach(System.out::println);
    }
}