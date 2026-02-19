package org.example.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.intershop.model.Cart;
import org.example.intershop.model.CartItem;
import org.example.intershop.model.Order;
import org.example.intershop.model.OrderItem;
import org.example.intershop.repository.OrderRepository;
import org.example.intershop.service.OrderItemService;
import org.example.intershop.service.OrderService;
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
        Order order = createOrder(cart);
        List<OrderItem> items = convertCartItemsToOrderItems(cart.getItems());

        return orderRepository.save(order)
                .doOnNext(savedOrder -> items.forEach(savedOrder::addItem))
                .flatMap(savedOrder -> orderItemService.saveAll(savedOrder.getItems())
                        .collectList()
                        .thenReturn(savedOrder))
                .doOnSuccess(savedOrder -> cart.clearCart());
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setTotalAmount(cart.calculateTotalAmount());

        return order;
    }

    private List<OrderItem> convertCartItemsToOrderItems(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new OrderItem(cartItem.getItem(), cartItem.getQuantity(), cartItem.getPrice()))
                .toList();
    }

    @Override
    public Mono<Long> getOrdersAmount(List<Order> orders) {
        long sum = orders.stream()
                .mapToLong(Order::getTotalAmount)
                .sum();

        return Mono.just(sum);
    }
}
