package dev.hashnode.ishbhana.order.service;

import dev.hashnode.ishbhana.order.domain.OutboxDomainEvent;
import dev.hashnode.ishbhana.order.publisher.RabbitEventPublisher;
import dev.hashnode.ishbhana.order.repository.OutboxDomainEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OutboxService {

    private final RabbitEventPublisher rabbitEventPublisher;
    private final OutboxDomainEventRepository outboxDomainEventRepository;

    public void sendFromOutbox(OutboxDomainEvent outboxDomainEvent) {
        rabbitEventPublisher.publishEvent(outboxDomainEvent.getDomainEvent());
        outboxDomainEventRepository.delete(outboxDomainEvent);
    }
}
