package com.nashtech.expenseapi.expense;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService service;

    @Test
    void createsValidExpense() throws Exception {
    when(service.create(any())).thenReturn(expenseResponse(1L));

    mockMvc.perform(post("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(validRequest()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.category").value("Technology"));
    }

    @Test
    void rejectsInvalidExpense() throws Exception {
    mockMvc.perform(post("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"description\":\"\",\"category\":\"\",\"amount\":0,\"expenseDate\":\"2030-01-01\",\"ownerEmail\":\"bad\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").isNotEmpty());
    }

    @Test
    void listsExpensesWithAllFiltersAndPagination() throws Exception {
    when(service.findAll(eq("Technology"), eq("cloud"), eq("buyer@example.com"), any()))
        .thenReturn(new PageImpl<>(List.of(expenseResponse(1L)), PageRequest.of(0, 10), 1));

    mockMvc.perform(get("/api/expenses")
            .param("category", "Technology")
            .param("description", "cloud")
            .param("ownerEmail", "buyer@example.com")
            .param("page", "0")
            .param("size", "10")
            .param("sortBy", "amount")
            .param("direction", "ASC"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].description").value("Cloud hosting"))
        .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void rejectsInvalidPaginationAndSortField() throws Exception {
    mockMvc.perform(get("/api/expenses").param("page", "-1"))
        .andExpect(status().isBadRequest());
    mockMvc.perform(get("/api/expenses").param("size", "101"))
        .andExpect(status().isBadRequest());
    mockMvc.perform(get("/api/expenses").param("sortBy", "id"))
        .andExpect(status().isBadRequest());
    }

    @Test
    void getsExpenseById() throws Exception {
    when(service.findById(1L)).thenReturn(expenseResponse(1L));

    mockMvc.perform(get("/api/expenses/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ownerEmail").value("buyer@example.com"));
    }

    @Test
    void returnsNotFoundForUnknownExpense() throws Exception {
    when(service.findById(999L)).thenThrow(new ExpenseNotFoundException(999L));

    mockMvc.perform(get("/api/expenses/999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.detail").value("Expense with id 999 was not found"));
    }

    @Test
    void updatesExpense() throws Exception {
    when(service.update(eq(1L), any())).thenReturn(expenseResponse(1L));

    mockMvc.perform(put("/api/expenses/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(validRequest()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deletesExpense() throws Exception {
    doNothing().when(service).delete(1L);

    mockMvc.perform(delete("/api/expenses/1"))
        .andExpect(status().isNoContent());

    verify(service).delete(1L);
    }

    @Test
    void returnsCategorySummary() throws Exception {
    when(service.categoryTotal("Technology")).thenReturn(new BigDecimal("49.99"));

    mockMvc.perform(get("/api/expenses/summary").param("category", "Technology"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(49.99));
    }

    private String validRequest() {
    return "{\"description\":\"Cloud hosting\",\"category\":\"Technology\","
        + "\"amount\":49.99,\"expenseDate\":\"2026-09-20\","
        + "\"ownerEmail\":\"buyer@example.com\"}";
    }

    private ExpenseResponse expenseResponse(Long id) {
    return new ExpenseResponse(id, "Cloud hosting", "Technology", new BigDecimal("49.99"),
        LocalDate.of(2026, 9, 20), "buyer@example.com");
    }
}