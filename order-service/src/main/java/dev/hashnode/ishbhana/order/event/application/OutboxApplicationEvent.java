package dev.hashnode.ishbhana.order.event.application;

import dev.hashnode.ishbhana.order.domain.OutboxDomainEvent;
import lombok.Value;

@Value
public class OutboxApplicationEvent {
    OutboxDomainEvent outboxDomainEvent;
}