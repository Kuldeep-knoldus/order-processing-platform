package com.nashtech.expenseapi.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByCategoryIgnoreCaseAndDescriptionContainingIgnoreCase(String category, String description, Pageable pageable);
    Page<Expense> findByOwnerEmailIgnoreCase(String ownerEmail, Pageable pageable);

        @Query("""
                        select e from Expense e
                        where (:category is null or lower(e.category) = lower(:category))
                            and (:description is null or lower(e.description) like lower(concat('%', :description, '%')))
                            and (:ownerEmail is null or lower(e.ownerEmail) = lower(:ownerEmail))
                        """)
        Page<Expense> findByFilters(@Param("category") String category,
                                                                @Param("description") String description,
                                                                @Param("ownerEmail") String ownerEmail,
                                                                Pageable pageable);

    @Query("select coalesce(sum(e.amount), 0) from Expense e where lower(e.category) = lower(:category)")
    BigDecimal sumByCategory(@Param("category") String category);
}