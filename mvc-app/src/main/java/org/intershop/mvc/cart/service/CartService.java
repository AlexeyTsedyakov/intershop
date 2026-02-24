package org.intershop.mvc.cart.service;

import org.intershop.mvc.cart.model.Cart;
import reactor.core.publisher.Mono;

public interface CartService {
    Mono<Long> getTotalAmount(Cart cart);

    Mono<Void> fetchItems(Cart cart);
}
