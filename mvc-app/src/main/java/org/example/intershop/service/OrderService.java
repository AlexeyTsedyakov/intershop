package org.example.intershop.service;

import org.example.intershop.model.Cart;
import org.example.intershop.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OrderService {
    Mono<Order> placeOrder(Cart cart);

    Mono<Order> findById(Long id);

    Flux<Order> findAll();

    Mono<Long> getOrdersAmount(List<Order> orders);
}
