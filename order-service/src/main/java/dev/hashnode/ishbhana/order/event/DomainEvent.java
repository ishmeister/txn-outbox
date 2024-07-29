package dev.hashnode.ishbhana.order.event;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode
public abstract class DomainEvent {
    @Builder.Default
    private String eventId = UUID.randomUUID().toString();
    @Builder.Default
    private Instant eventTimestamp = Instant.now();

    public abstract String getEventType();
}
