package org.example.intershop.service;

import org.example.intershop.model.OrderItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface OrderItemService {
    Flux<OrderItem> saveAll(Iterable<OrderItem> items);

    Flux<OrderItem> findAllByOrderId(Long orderId);

    Mono<Map<Long, List<OrderItem>>> findAllByOrderIdsAsMap(Collection<Long> orderIds);
}
