package com.nashtech.expenseapi.expense;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String category;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private String ownerEmail;

    protected Expense() { }

    public Expense(String description, String category, BigDecimal amount, LocalDate expenseDate, String ownerEmail) {
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.ownerEmail = ownerEmail;
    }

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getExpenseDate() { return expenseDate; }
    public String getOwnerEmail() { return ownerEmail; }

    public void update(String description, String category, BigDecimal amount, LocalDate expenseDate, String ownerEmail) {
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.ownerEmail = ownerEmail;
    }
}