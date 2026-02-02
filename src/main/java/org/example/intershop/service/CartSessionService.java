package org.example.intershop.service;

import org.example.intershop.model.Cart;
import org.springframework.web.server.WebSession;

public interface CartSessionService {
    Cart getCart(WebSession session);
}
