package org.example.intershop.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@SessionScope
public class Cart {
    private final List<CartItem> cartItems = new ArrayList<>();

    public List<CartItem> getItems() {
        return List.copyOf(cartItems);
    }

    public int getQuantity(Long itemId) {
        Optional<CartItem> cartItem = findCartItem(itemId);
        return cartItem.map(CartItem::getQuantity).orElse(0);
    }

    public void increaseQuantity(Long itemId) {
        CartItem cartItem = findCartItem(itemId).orElseGet(() -> {
            var newItem = new CartItem(itemId, 0, 0);
            cartItems.add(newItem);
            return newItem;
        });

        cartItem.setQuantity(cartItem.getQuantity() + 1);
    }

    public void decreaseQuantity(Long itemId) {
        findCartItem(itemId).ifPresent(cartItem -> {
            int quantity = cartItem.getQuantity();
            if (quantity <= 1) {
                cartItems.remove(cartItem);
            } else {
                cartItem.setQuantity(quantity - 1);
            }
        });
    }

    public void removeItem(Long itemId) {
        cartItems.removeIf(item -> itemId.equals(item.getItemId()));
    }

    public void clearCart() {
        cartItems.clear();
    }

    private Optional<CartItem> findCartItem(Long itemId) {
        return cartItems.stream()
                .filter(item -> itemId.equals(item.getItemId()))
                .findFirst();
    }
}