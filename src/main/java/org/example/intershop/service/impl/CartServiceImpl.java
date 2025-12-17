package org.example.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.intershop.model.Cart;
import org.example.intershop.model.CartItem;
import org.example.intershop.model.Item;
import org.example.intershop.service.CartService;
import org.example.intershop.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {
    private final ItemService itemService;

    @Override
    public long getTotalAmount(Cart cart) {
        if (hasNullItems(cart)) {
            fetchItems(cart);
        }

        return calculateTotalAmount(cart);
    }

    @Override
    public void fetchItems(Cart cart) {
        Map<Long, CartItem> snapshot = cart.getSnapshot();
        Set<Long> ids = snapshot.keySet();
        Collection<CartItem> cartItems = snapshot.values();
        Map<Long, Item> itemsById = itemService.findAllByIds(ids);

        if (ids.size() != itemsById.size()) {
            throw new RuntimeException("Some items were not found");
        }

        cartItems.forEach(cartItem -> cartItem.setItem(itemsById.get(cartItem.getItemId())));
    }

    private boolean hasNullItems(Cart cart) {
        return cart.getSnapshot().values().stream()
                .anyMatch(cartItem -> cartItem.getItem() == null);
    }

    private long calculateTotalAmount(Cart cart) {
        return cart.getSnapshot().values().stream()
                .mapToLong(cartItem -> cartItem.getQuantity() * (long) cartItem.getItem().getPrice())
                .sum();
    }
}
