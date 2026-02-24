package org.intershop.mvc.cart.service;

import org.intershop.mvc.cart.model.Cart;
import org.springframework.web.server.WebSession;

public interface CartSessionService {
    Cart getCart(WebSession session);
}
