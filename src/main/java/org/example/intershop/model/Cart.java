package org.example.intershop.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Component
@SessionScope
public class Cart {
    private final Map<Long, Integer> quantitiesByItemId = new HashMap<>();

    public Map<Long, Integer> getSnapshot() {
        return quantitiesByItemId;
    }

    public int getQuantity(Long itemId) {
        return quantitiesByItemId.getOrDefault(itemId, 0);
    }

    public void increaseQuantity(Long itemId) {
        quantitiesByItemId.merge(itemId, 1, (Integer::sum));
    }

    public void decreaseQuantity(Long itemId) {
        quantitiesByItemId.computeIfPresent(itemId, (id, qty) -> qty <= 1 ? null : qty - 1);
    }

    public void removeItem(Long itemId) {
        quantitiesByItemId.remove(itemId);
    }

    public void clearCart() {
        quantitiesByItemId.clear();
    }
}