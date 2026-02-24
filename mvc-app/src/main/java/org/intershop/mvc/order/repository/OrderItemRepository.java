package org.intershop.mvc.order.repository;

import org.intershop.mvc.order.model.OrderItem;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface OrderItemRepository extends R2dbcRepository<OrderItem, Long> {
    Flux<OrderItem> findAllByOrderId(Long orderId);

    Flux<OrderItem> findAllByOrderIdIsIn(Collection<Long> orderId);
}
