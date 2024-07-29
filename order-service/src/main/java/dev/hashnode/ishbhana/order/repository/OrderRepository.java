package dev.hashnode.ishbhana.order.repository;

import dev.hashnode.ishbhana.order.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
