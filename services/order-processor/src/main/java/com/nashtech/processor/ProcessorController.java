package com.nashtech.processor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/processing")
public class ProcessorController {
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProcessingResponse process(@RequestBody ProcessOrderRequest request) {
        return new ProcessingResponse(request.orderId(), "PROCESSED", Instant.now());
    }

    @GetMapping("/health")
    public String health() { return "order-processor is healthy"; }

    public record ProcessOrderRequest(UUID orderId) {}
    public record ProcessingResponse(UUID orderId, String status, Instant processedAt) {}
}