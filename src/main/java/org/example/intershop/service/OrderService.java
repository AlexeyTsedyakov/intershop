package org.example.intershop.service;

import org.example.intershop.model.Cart;
import org.example.intershop.model.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(Cart cart);

    Order findById(Long id);

    List<Order> findAll();

    long getOrdersAmount(List<Order> orders);
}
