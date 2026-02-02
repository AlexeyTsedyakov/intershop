package org.example.intershop.service;

import org.example.intershop.model.Cart;
import reactor.core.publisher.Mono;

public interface CartService {
    Mono<Long> getTotalAmount(Cart cart);

    Mono<Void> fetchItems(Cart cart);
}
