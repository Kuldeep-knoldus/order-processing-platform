package com.nashtech.expenseapi.expense;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean ExpenseService service;

    @Test
    void createsValidExpense() throws Exception {
        ExpenseResponse response = new ExpenseResponse(1L, "Cloud hosting", "Technology",
                new BigDecimal("49.99"), LocalDate.of(2026, 9, 20), "buyer@example.com");
        when(service.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/expenses").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Cloud hosting\",\"category\":\"Technology\",\"amount\":49.99,\"expenseDate\":\"2026-09-20\",\"ownerEmail\":\"buyer@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.category").value("Technology"));
    }

    @Test
    void rejectsInvalidExpense() throws Exception {
        mockMvc.perform(post("/api/expenses").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"\",\"category\":\"\",\"amount\":0,\"expenseDate\":\"2030-01-01\",\"ownerEmail\":\"bad\"}"))
                .andExpect(status().isBadRequest());
    }
}