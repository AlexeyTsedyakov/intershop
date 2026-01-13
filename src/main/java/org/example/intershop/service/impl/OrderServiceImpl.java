package org.example.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.intershop.model.Cart;
import org.example.intershop.model.Order;
import org.example.intershop.model.OrderItem;
import org.example.intershop.repository.OrderRepository;
import org.example.intershop.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Order placeOrder(Cart cart) {
        Order order = createOrder(cart);
        orderRepository.save(order);
        cart.clearCart();

        return order;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        cart.getItems().stream()
                .map(cartItem -> new OrderItem(cartItem.getItem(), cartItem.getQuantity(), cartItem.getPrice()))
                .forEach(order::addItem);
        order.setTotalAmount(order.calculateTotalAmount());

        return order;
    }

    @Override
    public long getOrdersAmount(List<Order> orders) {
        return orders.stream()
                .mapToLong(Order::getTotalAmount)
                .sum();
    }
}
