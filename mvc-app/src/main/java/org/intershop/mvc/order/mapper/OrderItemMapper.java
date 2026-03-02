package org.intershop.mvc.order.mapper;

import org.intershop.mvc.cart.model.CartItem;
import org.intershop.mvc.order.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderItemMapper {
    public List<OrderItem> fromCartItems(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new OrderItem(cartItem.getItem(), cartItem.getQuantity(), cartItem.getPrice()))
                .toList();
    }
}
