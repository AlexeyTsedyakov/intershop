package org.intershop.mvc.order.factory;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.cart.model.Cart;
import org.intershop.mvc.order.mapper.OrderItemMapper;
import org.intershop.mvc.order.model.Order;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderFactory {
    private final OrderItemMapper orderItemMapper;

    public Order createFromCart(Cart cart) {
        Order order = new Order();
        order.setTotalAmount(cart.calculateTotalAmount());
        order.setItems(orderItemMapper.fromCartItems(cart.getItems()));
        order.setUserId(cart.getUserId());

        return order;
    }
}
