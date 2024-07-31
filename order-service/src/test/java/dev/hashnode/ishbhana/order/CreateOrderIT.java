package dev.hashnode.ishbhana.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hashnode.ishbhana.order.domain.OutboxDomainEvent;
import dev.hashnode.ishbhana.order.dto.CreateOrderRequestDto;
import dev.hashnode.ishbhana.order.event.DomainEvent;
import dev.hashnode.ishbhana.order.event.OrderCreatedEvent;
import dev.hashnode.ishbhana.order.publisher.RabbitEventPublisher;
import dev.hashnode.ishbhana.order.repository.OutboxDomainEventRepository;
import dev.hashnode.ishbhana.order.scheduler.OutboxScheduler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
@ContextConfiguration(classes = {TestcontainersConfig.class, IntegrationTestConfig.class})
@Tag("integration")
@ActiveProfiles({"it"})
class CreateOrderIT {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @LocalServerPort
    int localServerPort;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Queue orderCreatedQueue;

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    OutboxDomainEventRepository outboxDomainEventRepository;

    @Autowired
    OutboxScheduler outboxScheduler;

    @SpyBean
    RabbitEventPublisher rabbitEventPublisher;

    @AfterEach
    void afterEach() {
        amqpAdmin.purgeQueue(orderCreatedQueue.getName());
        outboxDomainEventRepository.deleteAll();
    }

    @Test
    void createOrder_shouldCreateOrderAndPublishDomainEvent() {
        CreateOrderRequestDto request =
                new CreateOrderRequestDto("1234", new BigDecimal("99.99"));

        webTestClient.post()
                .uri(baseUri() + "/api/orders")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.customerId").isEqualTo(request.customerId())
                .jsonPath("$.orderDate").isNotEmpty()
                .jsonPath("$.totalAmount").isEqualTo(request.totalAmount());

        await().atMost(5, SECONDS).untilAsserted(() -> {
            OrderCreatedEvent event = getEvent(orderCreatedQueue);
            assertThat(event).isNotNull();
            assertThat(event.getEventId()).isNotBlank();
            assertThat(event.getOrderId()).isNotBlank();
            assertThat(event.getEventTimestamp()).isNotNull();
        });

        List<OutboxDomainEvent> eventList = outboxDomainEventRepository.findAll();
        assertThat(eventList).hasSize(0);
    }

    @Test
    void createOrder_shouldCreateOrderAndRetainDomainEventInOutboxWhenRabbitFails() {
        doThrow(new RuntimeException("error"))
                .when(rabbitEventPublisher)
                .publishEvent(any(DomainEvent.class));

        CreateOrderRequestDto request =
                new CreateOrderRequestDto("1234", new BigDecimal("99.99"));

        webTestClient.post()
                .uri(baseUri() + "/api/orders")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.customerId").isEqualTo(request.customerId())
                .jsonPath("$.orderDate").isNotEmpty()
                .jsonPath("$.totalAmount").isEqualTo(request.totalAmount());

        List<OutboxDomainEvent> eventList = outboxDomainEventRepository.findAll();
        assertThat(eventList).hasSize(1);
    }

    @Test
    void createOrder_shouldPublishDomainEventFromOutbox() {
        String orderId = "54321";

        OrderCreatedEvent domainEvent = OrderCreatedEvent.builder()
                .orderId(orderId)
                .eventTimestamp(Instant.now()
                        .minus(31, ChronoUnit.SECONDS))
                .build();

        outboxDomainEventRepository.save(OutboxDomainEvent.builder()
                .domainEvent(domainEvent)
                .build());

        outboxScheduler.processOutbox();

        await().atMost(5, SECONDS).untilAsserted(() -> {
            OrderCreatedEvent event = getEvent(orderCreatedQueue);
            assertThat(event).isNotNull();
            assertThat(event.getEventId()).isNotBlank();
            assertThat(event.getOrderId()).isEqualTo(orderId);
            assertThat(event.getEventTimestamp()).isNotNull();
        });

        List<OutboxDomainEvent> eventList = outboxDomainEventRepository.findAll();
        assertThat(eventList).hasSize(0);
    }

    String baseUri() {
        return "http://localhost:" + localServerPort;
    }

    @SuppressWarnings("unchecked")
    <T> T getEvent(Queue queue) {
        return (T) rabbitTemplate.receiveAndConvert(queue.getName());
    }
}
