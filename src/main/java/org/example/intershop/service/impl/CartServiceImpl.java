package org.example.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.intershop.model.Cart;
import org.example.intershop.model.CartItem;
import org.example.intershop.model.Item;
import org.example.intershop.service.CartService;
import org.example.intershop.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        List<Long> ids = getItemsIds(cart);
        Map<Long, Item> itemsById = itemService.findAllByIdsAsMap(ids);
        if (ids.size() != itemsById.size()) {
            throw new RuntimeException("Some items were not found");
        }

        cart.getItems().forEach(cartItem -> {
            Item item = itemsById.get(cartItem.getItemId());
            cartItem.setItem(item);
            cartItem.setPrice(item.getPrice());
        });
    }

    private List<Long> getItemsIds(Cart cart) {
        return cart.getItems().stream()
                .map(CartItem::getItemId)
                .toList();
    }

    private boolean hasNullItems(Cart cart) {
        return cart.getItems().stream()
                .anyMatch(cartItem -> cartItem.getItem() == null);
    }

    private long calculateTotalAmount(Cart cart) {
        return cart.getItems().stream()
                .mapToLong(cartItem -> cartItem.getQuantity() * (long) cartItem.getPrice())
                .sum();
    }
}
