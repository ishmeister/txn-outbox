package dev.hashnode.ishbhana.order.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderCreatedEvent extends DomainEvent {

    public static final String EVENT_TYPE = "order.created";

    @NonNull
    private String orderId;

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }
}


