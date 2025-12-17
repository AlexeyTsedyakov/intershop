package org.example.intershop.service;

import org.example.intershop.model.Cart;

public interface CartService {
    long getTotalAmount(Cart cart);

    void fetchItems(Cart cart);
}
