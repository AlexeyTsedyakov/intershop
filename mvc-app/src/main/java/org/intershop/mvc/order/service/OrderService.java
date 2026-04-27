package org.intershop.mvc.order.service;

import org.intershop.mvc.cart.model.Cart;
import org.intershop.mvc.order.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OrderService {
    Mono<Order> placeOrder(Cart cart);

    Mono<Order> findById(Long id);

    Flux<Order> findAllByUserId(Long id);

    Mono<Long> getOrdersAmount(List<Order> orders);
}
