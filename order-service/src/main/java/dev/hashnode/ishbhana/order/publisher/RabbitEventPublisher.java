package dev.hashnode.ishbhana.order.publisher;

import dev.hashnode.ishbhana.order.event.DomainEvent;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RabbitEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Exchange eventExchange;

    public void publishEvent(DomainEvent domainEvent) {
        String routingKey = getRoutingKey(domainEvent);
        rabbitTemplate.convertAndSend(eventExchange.getName(), routingKey, domainEvent);
    }

    private String getRoutingKey(DomainEvent domainEvent) {
        return "route." + domainEvent.getEventType() + ".#";
    }
}
