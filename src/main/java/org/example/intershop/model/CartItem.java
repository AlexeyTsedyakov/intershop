package org.example.intershop.model;

import lombok.Data;

@Data
public class CartItem {
    private long itemId;
    private Item item;
    private int quantity;

    public CartItem(long itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }
}
