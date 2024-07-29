package dev.hashnode.ishbhana.order.dto;

import java.time.Instant;

public record CreateOrderResponseDto(
        String id,
        String customerId,
        Instant orderDate,
        double totalAmount
) {
}
