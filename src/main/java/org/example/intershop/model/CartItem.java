package org.example.intershop.model;

import lombok.Data;

@Data
public class CartItem {
    private long itemId;
    private Item item;
    private int quantity;
    private int price;

    public CartItem(long itemId, int quantity, int price) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
    }
}
