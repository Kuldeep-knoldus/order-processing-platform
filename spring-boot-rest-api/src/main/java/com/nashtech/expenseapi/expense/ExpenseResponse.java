package com.nashtech.expenseapi.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse(Long id, String description, String category, BigDecimal amount,
                              LocalDate expenseDate, String ownerEmail) {
    public static ExpenseResponse from(Expense expense) {
        return new ExpenseResponse(expense.getId(), expense.getDescription(), expense.getCategory(),
                expense.getAmount(), expense.getExpenseDate(), expense.getOwnerEmail());
    }
}