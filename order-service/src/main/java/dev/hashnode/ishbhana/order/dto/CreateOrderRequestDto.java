package dev.hashnode.ishbhana.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateOrderRequestDto(
        @NotBlank
        String customerId,
        @NotNull
        BigDecimal totalAmount
) {
}
