package org.intershop.mvc.cart.model;

import lombok.Data;
import org.intershop.mvc.item.model.Item;

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

    public CartItem(Item item, int quantity, int price) {
        this.itemId = item.getId();
        this.quantity = quantity;
        this.price = price;
        this.item = item;
    }
}
