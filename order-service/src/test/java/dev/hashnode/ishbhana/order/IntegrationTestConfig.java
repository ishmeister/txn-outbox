package dev.hashnode.ishbhana.order;

import dev.hashnode.ishbhana.order.event.OrderCreatedEvent;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class IntegrationTestConfig {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return QueueBuilder.nonDurable("queue.shipping." +
                OrderCreatedEvent.EVENT_TYPE).build();
    }

    @Bean
    public Binding orderCreatedQueueBinding(
            TopicExchange eventExchange, Queue orderCreatedQueue) {
        return BindingBuilder
                .bind(orderCreatedQueue)
                .to(eventExchange)
                .with("route." + OrderCreatedEvent.EVENT_TYPE + ".#");
    }
}
