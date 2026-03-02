package org.intershop.mvc.order.service;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.cart.model.Cart;
import org.intershop.mvc.order.factory.OrderFactory;
import org.intershop.mvc.order.model.Order;
import org.intershop.mvc.order.model.OrderItem;
import org.intershop.mvc.order.repository.OrderRepository;
import org.intershop.mvc.payment.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final PaymentService paymentService;
    private final OrderFactory orderFactory;

    @Override
    @Transactional(readOnly = true)
    public Mono<Order> findById(Long id) {
        return orderRepository.findById(id)
                .zipWith(orderItemService.findAllByOrderId(id).collectList())
                .map(tuple -> {
                    Order order = tuple.getT1();
                    List<OrderItem> items = tuple.getT2();
                    order.setItems(items);

                    return order;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Order> findAll() {
        return orderRepository.findAll()
                .collectList()
                .flatMap(this::enrichOrders)
                .flatMapMany(Flux::fromIterable);
    }

    private Mono<List<Order>> enrichOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            return Mono.just(orders);
        }

        List<Long> ids = orders.stream()
                .map(Order::getId)
                .toList();

        return orderItemService.findAllByOrderIdsAsMap(ids)
                .map(itemsById -> {
                    orders.forEach(order ->
                            order.setItems(itemsById.getOrDefault(order.getId(), new ArrayList<>())));
                    return orders;
                });
    }

    @Override
    @Transactional
    public Mono<Order> placeOrder(Cart cart) {
        Order order = orderFactory.createFromCart(cart);

        return paymentService.processPayment(order.getTotalAmount())
                .then(orderRepository.save(order))
                .doOnNext(savedOrder -> savedOrder.getItems()
                        .forEach(item -> item.setOrderId(savedOrder.getId())))
                .flatMap(savedOrder -> orderItemService.saveAll(savedOrder.getItems())
                        .then(Mono.just(savedOrder)))
                .doOnSuccess(savedOrder -> cart.clearCart());
    }

    @Override
    public Mono<Long> getOrdersAmount(List<Order> orders) {
        long sum = orders.stream()
                .mapToLong(Order::getTotalAmount)
                .sum();

        return Mono.just(sum);
    }
}
