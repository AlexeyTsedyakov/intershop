package org.example.intershop.service.impl;

import org.example.intershop.model.Cart;
import org.example.intershop.service.CartSessionService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.WebSession;

@Service
public class CartSessionServiceImpl implements CartSessionService {
    private static final String CART_SESSION_KEY = "cart";

    @Override
    public Cart getCart(WebSession session) {
        Cart cart = session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new Cart();
            session.getAttributes().put(CART_SESSION_KEY, cart);
        }

        return cart;
    }
}
