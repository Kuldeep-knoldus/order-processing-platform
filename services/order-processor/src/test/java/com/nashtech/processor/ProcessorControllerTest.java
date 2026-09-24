package com.nashtech.processor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProcessorController.class)
class ProcessorControllerTest {
    @Autowired MockMvc mockMvc;

    @Test
    void processesOrder() throws Exception {
        mockMvc.perform(post("/api/processing").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":\"550e8400-e29b-41d4-a716-446655440000\"}"))
                .andExpect(status().isAccepted()).andExpect(jsonPath("$.status").value("PROCESSED"));
    }
}