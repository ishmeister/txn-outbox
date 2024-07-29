package dev.hashnode.ishbhana.order.listener;

import dev.hashnode.ishbhana.order.event.application.OutboxApplicationEvent;
import dev.hashnode.ishbhana.order.service.OutboxService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@AllArgsConstructor
public class OutboxApplicationEventListener {

    private final OutboxService outboxService;

    @TransactionalEventListener
    public void onOutboxApplicationEvent(OutboxApplicationEvent appEvent) {
        outboxService.sendFromOutbox(appEvent.getOutboxDomainEvent());
    }
}
