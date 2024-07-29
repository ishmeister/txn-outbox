package dev.hashnode.ishbhana.order.domain;

import dev.hashnode.ishbhana.order.event.DomainEvent;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Immutable;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Value
@Immutable
@Document(collection = "outbox_domain_event")
public class OutboxDomainEvent {
    @Id
    String id;
    DomainEvent domainEvent;
}
