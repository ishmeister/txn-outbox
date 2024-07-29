package dev.hashnode.ishbhana.order.scheduler;

import dev.hashnode.ishbhana.order.domain.OutboxDomainEvent;
import dev.hashnode.ishbhana.order.repository.OutboxDomainEventRepository;
import dev.hashnode.ishbhana.order.service.OutboxService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class OutboxScheduler {

    private final OutboxDomainEventRepository outboxDomainEventRepository;
    private final OutboxService outboxService;

    @Scheduled(fixedRate = 30000)
    public void processOutbox() {
        Instant oneMinuteAgo = Instant.now().minus(30, ChronoUnit.SECONDS);
        Sort sort = Sort.by(Sort.Direction.ASC, "domainEvent.eventTimestamp");
        PageRequest pageRequest = PageRequest.of(0, 100, sort);

        List<OutboxDomainEvent> events = outboxDomainEventRepository
                .findOlderThan(oneMinuteAgo, pageRequest);

        log.debug("processing {} outbox events", events.size());
        events.forEach(outboxService::sendFromOutbox);
    }
}
