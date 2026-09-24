package com.nashtech.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return new OrderResponse(UUID.randomUUID(), "ACCEPTED", request.customerEmail(), request.product(), request.quantity(), Instant.now());
    }

    @GetMapping("/health")
    public String health() { return "order-api is healthy"; }

    public record CreateOrderRequest(@NotBlank @Email String customerEmail, @NotBlank String product, @Positive int quantity) {}
    public record OrderResponse(UUID orderId, String status, String customerEmail, String product, int quantity, Instant createdAt) {}
}