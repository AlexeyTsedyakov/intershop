package org.example.intershop.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@SessionScope
public class Cart {
    private final Map<Long, CartItem> cartItemByItemId = new LinkedHashMap<>();

    public Map<Long, CartItem> getSnapshot() {
        return Map.copyOf(cartItemByItemId);
    }

    public int getQuantity(Long itemId) {
        CartItem cartItem = cartItemByItemId.get(itemId);
        return cartItem == null ? 0 : cartItem.getQuantity();
    }

    public void increaseQuantity(Long itemId) {
        CartItem cartItem = cartItemByItemId.computeIfAbsent(itemId, (id) -> new CartItem(id, 0));
        cartItem.setQuantity(cartItem.getQuantity() + 1);
    }

    public void decreaseQuantity(Long itemId) {
        CartItem cartItem = cartItemByItemId.computeIfPresent(itemId,
                (id, item) -> item.getQuantity() <= 1 ? null : item);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
        }
    }

    public void removeItem(Long itemId) {
        cartItemByItemId.remove(itemId);
    }

    public void clearCart() {
        cartItemByItemId.clear();
    }
}