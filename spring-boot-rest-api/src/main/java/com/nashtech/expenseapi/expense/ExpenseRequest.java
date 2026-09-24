package com.nashtech.expenseapi.expense;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
        @NotBlank(message = "description is required") String description,
        @NotBlank(message = "category is required") String category,
        @NotNull @DecimalMin(value = "0.01", message = "amount must be greater than zero") BigDecimal amount,
        @NotNull @PastOrPresent(message = "expenseDate cannot be in the future") LocalDate expenseDate,
        @NotBlank @Email(message = "ownerEmail must be valid") String ownerEmail
) { }