package org.intershop.mvc.cart.model;

import org.intershop.mvc.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart {
    private final List<CartItem> cartItems = new ArrayList<>();

    public List<CartItem> getItems() {
        return List.copyOf(cartItems);
    }

    public CartItem getItemById(Long id) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.getItemId() == id)
                .findFirst()
                .orElse(null);
    }

    public int getQuantity(Long itemId) {
        Optional<CartItem> cartItem = findCartItem(itemId);
        return cartItem.map(CartItem::getQuantity).orElse(0);
    }

    public void increaseQuantity(Item item) {
        CartItem cartItem = findCartItem(item.getId()).orElseGet(() -> {
            var newItem = new CartItem(item, 0, item.getPrice());
            cartItems.add(newItem);
            return newItem;
        });

        cartItem.setQuantity(cartItem.getQuantity() + 1);
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

    public long calculateTotalAmount() {
        return cartItems.stream()
                .mapToLong(item -> (long) item.getPrice() * item.getQuantity())
                .sum();
    }

    public List<Long> getItemsIds() {
        return cartItems.stream()
                .map(CartItem::getItemId)
                .toList();
    }

    public boolean hasNullItems() {
        return cartItems.stream()
                .anyMatch(cartItem -> cartItem.getItem() == null);
    }
}