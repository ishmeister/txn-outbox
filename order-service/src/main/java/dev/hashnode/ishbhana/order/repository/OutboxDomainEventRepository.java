package dev.hashnode.ishbhana.order.repository;

import dev.hashnode.ishbhana.order.domain.OutboxDomainEvent;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OutboxDomainEventRepository extends MongoRepository<OutboxDomainEvent, String> {
    @Query("{ 'domainEvent.eventTimestamp': { $lt: ?0 } }")
    List<OutboxDomainEvent> findOlderThan(Instant threshold, PageRequest pageRequest);
}
