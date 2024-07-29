package dev.hashnode.ishbhana.order.domain;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Immutable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
@Immutable
@Document(collection = "order")
public class Order {
    @Id
    String id;
    String customerId;
    @With
    Instant orderDate;
    BigDecimal totalAmount;
}
