package org.intershop.mvc.cart.controller;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.cart.service.CartService;
import org.intershop.mvc.cart.service.CartSessionService;
import org.intershop.mvc.cart.model.Cart;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final CartSessionService cartSessionService;

    @GetMapping
    public Mono<Rendering> showCart(WebSession session) {
        Cart cart = cartSessionService.getCart(session);

        return cartService.fetchItems(cart)
                .then(cartService.getTotalAmount(cart))
                .map(totalAmount -> Rendering.view("cart")
                        .modelAttribute("cart", cart)
                        .modelAttribute("totalAmount", totalAmount)
                        .build());
    }

    @PostMapping("/increase/{id}")
    public Mono<Rendering> add(@PathVariable Long id, @RequestHeader(value = HttpHeaders.REFERER) String referer,
                               WebSession session) {
        Cart cart = cartSessionService.getCart(session);

        return Mono.fromRunnable(() -> cart.increaseQuantity(id))
                .then(redirectToReferer(referer));
    }

    @PostMapping("/decrease/{id}")
    public Mono<Rendering> decrease(@PathVariable Long id, @RequestHeader(value = HttpHeaders.REFERER) String referer,
                                    WebSession session) {
        Cart cart = cartSessionService.getCart(session);

        return Mono.fromRunnable(() -> cart.decreaseQuantity(id))
                .then(redirectToReferer(referer));
    }

    @PostMapping("/remove/{id}")
    public Mono<Rendering> remove(@PathVariable Long id, @RequestHeader(value = HttpHeaders.REFERER) String referer,
                                  WebSession session) {
        Cart cart = cartSessionService.getCart(session);

        return Mono.fromRunnable(() -> cart.removeItem(id))
                .then(redirectToReferer(referer));
    }

    @PostMapping("/clear")
    public Mono<Rendering> clear(@RequestHeader(value = HttpHeaders.REFERER) String referer, WebSession session) {
        Cart cart = cartSessionService.getCart(session);

        return Mono.fromRunnable(cart::clearCart)
                .then(redirectToReferer(referer));
    }

    private Mono<Rendering> redirectToReferer(String referer) {
        // Если заголовка нет, возвращаемся на главную витрину
        if (referer == null || referer.isBlank()) {
            return Mono.just(Rendering.redirectTo("/items").build());
        }

        return Mono.just(Rendering.redirectTo(referer).build());
    }
}
