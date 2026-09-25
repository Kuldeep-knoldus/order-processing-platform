package com.nashtech.expenseapi.expense;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequestMapping("/api/expenses")
@Validated
public class ExpenseController {
    private static final Set<String> SORTABLE_FIELDS = Set.of("expenseDate", "description", "category", "amount", "ownerEmail");
    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse create(@Valid @RequestBody ExpenseRequest request) {
        return service.create(request);
    }

    @GetMapping
    public Page<ExpenseResponse> findAll(@RequestParam(required = false) String category,
                                         @RequestParam(required = false) String description,
                                         @RequestParam(required = false) String ownerEmail,
                                         @RequestParam(defaultValue = "0") @Min(0) int page,
                                         @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
                                         @RequestParam(defaultValue = "expenseDate") String sortBy,
                                         @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        if (!SORTABLE_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Unsupported sort field: " + sortBy);
        }
        return service.findAll(category, description, ownerEmail,
                PageRequest.of(page, size, Sort.by(direction, sortBy)));
    }

    @GetMapping("/{id}")
    public ExpenseResponse findById(@PathVariable Long id) { return service.findById(id); }

    @GetMapping("/summary")
    public BigDecimal categoryTotal(@RequestParam String category) { return service.categoryTotal(category); }

    @PutMapping("/{id}")
    public ExpenseResponse update(@PathVariable Long id, @Valid @RequestBody ExpenseRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}