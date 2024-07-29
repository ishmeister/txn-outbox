package dev.hashnode.ishbhana.order.publisher;

import dev.hashnode.ishbhana.order.domain.OutboxDomainEvent;
import dev.hashnode.ishbhana.order.event.DomainEvent;
import dev.hashnode.ishbhana.order.event.application.OutboxApplicationEvent;
import dev.hashnode.ishbhana.order.repository.OutboxDomainEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final OutboxDomainEventRepository outboxDomainEventRepository;

    @Transactional
    public void publishEvent(DomainEvent domainEvent) {
        OutboxDomainEvent outboxDomainEvent = outboxDomainEventRepository.save(
                OutboxDomainEvent.builder()
                        .domainEvent(domainEvent)
                        .build()
        );

        applicationEventPublisher.publishEvent(
                new OutboxApplicationEvent(outboxDomainEvent));
    }
}
