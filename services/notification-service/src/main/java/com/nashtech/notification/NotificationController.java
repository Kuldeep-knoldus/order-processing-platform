package com.nashtech.notification;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public NotificationResponse send(@RequestBody NotificationRequest request) {
        return new NotificationResponse(request.orderId(), "QUEUED", Instant.now());
    }

    @GetMapping("/health")
    public String health() { return "notification-service is healthy"; }

    public record NotificationRequest(UUID orderId, String recipient, String message) {}
    public record NotificationResponse(UUID orderId, String status, Instant queuedAt) {}
}