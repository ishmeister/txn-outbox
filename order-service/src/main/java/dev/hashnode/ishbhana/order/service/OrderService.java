package dev.hashnode.ishbhana.order.service;

import dev.hashnode.ishbhana.order.domain.Order;
import dev.hashnode.ishbhana.order.dto.CreateOrderRequestDto;
import dev.hashnode.ishbhana.order.event.OrderCreatedEvent;
import dev.hashnode.ishbhana.order.mapper.OrderMapper;
import dev.hashnode.ishbhana.order.publisher.DomainEventPublisher;
import dev.hashnode.ishbhana.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public Order createOrder(CreateOrderRequestDto request) {
        Order savedOrder = orderRepository.save(
                orderMapper.toOrder(request)
                        .withOrderDate(Instant.now())
        );

        domainEventPublisher.publishEvent(
                OrderCreatedEvent.builder()
                        .orderId(savedOrder.getId())
                        .build()
        );

        return savedOrder;
    }
}
