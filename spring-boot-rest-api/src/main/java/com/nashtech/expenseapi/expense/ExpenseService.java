package com.nashtech.expenseapi.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class ExpenseService {
    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public ExpenseResponse create(ExpenseRequest request) {
        return ExpenseResponse.from(repository.save(toEntity(request)));
    }

    @Transactional(readOnly = true)
    public Page<ExpenseResponse> findAll(String category, String description, String ownerEmail, Pageable pageable) {
        String normalizedCategory = normalize(category);
        String normalizedDescription = normalize(description);
        String normalizedOwnerEmail = normalize(ownerEmail);
        Page<Expense> expenses;
        if (normalizedCategory == null && normalizedDescription == null && normalizedOwnerEmail == null) {
            expenses = repository.findAll(pageable);
        } else if (normalizedCategory != null && normalizedDescription != null && normalizedOwnerEmail == null) {
            expenses = repository.findByCategoryIgnoreCaseAndDescriptionContainingIgnoreCase(
                    normalizedCategory, normalizedDescription, pageable);
        } else if (normalizedCategory == null && normalizedDescription == null) {
            expenses = repository.findByOwnerEmailIgnoreCase(normalizedOwnerEmail, pageable);
        } else {
            expenses = repository.findByFilters(normalizedCategory, normalizedDescription,
                    normalizedOwnerEmail, pageable);
        }
        return expenses.map(ExpenseResponse::from);
    }

    @Transactional(readOnly = true)
    public ExpenseResponse findById(Long id) {
        return ExpenseResponse.from(getExpense(id));
    }

    public ExpenseResponse update(Long id, ExpenseRequest request) {
        Expense expense = getExpense(id);
        expense.update(request.description(), request.category(), request.amount(), request.expenseDate(), request.ownerEmail());
        return ExpenseResponse.from(expense);
    }

    public void delete(Long id) {
        repository.delete(getExpense(id));
    }

    @Transactional(readOnly = true)
    public BigDecimal categoryTotal(String category) {
        return repository.sumByCategory(category);
    }

    private Expense getExpense(Long id) {
        return repository.findById(id).orElseThrow(() -> new ExpenseNotFoundException(id));
    }

    private Expense toEntity(ExpenseRequest request) {
        return new Expense(request.description(), request.category(), request.amount(), request.expenseDate(), request.ownerEmail());
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}