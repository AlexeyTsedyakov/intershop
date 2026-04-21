package org.intershop.mvc.cart.controller;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.cart.model.Cart;
import org.intershop.mvc.cart.service.CartService;
import org.intershop.mvc.cart.service.CartSessionService;
import org.intershop.mvc.payment.service.BalanceService;
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
    private final BalanceService balanceService;

    @GetMapping
    public Mono<Rendering> showCart(WebSession session) {
        Cart cart = cartSessionService.getCart(session);

        return cartService.fetchItems(cart)
                .then(Mono.zip(
                        cartService.getTotalAmount(cart),
                        balanceService.getBalance()
                ))
                .map(tuple -> Rendering.view("cart")
                        .modelAttribute("cart", cart)
                        .modelAttribute("totalAmount", tuple.getT1())
                        .modelAttribute("balance", tuple.getT2().value())
                        .modelAttribute("balanceError", tuple.getT2().errorMessage())
                        .build());
    }

    @PostMapping("/increase/{id}")
    public Mono<Rendering> add(@PathVariable Long id, @RequestParam String redirectUrl, WebSession session) {
        Cart cart = cartSessionService.getCart(session);

        return Mono.fromRunnable(() -> cart.increaseQuantity(id))
                .then(redirect(redirectUrl));
    }

    @PostMapping("/decrease/{id}")
    public Mono<Rendering> decrease(@PathVariable Long id, @RequestParam String redirectUrl, WebSession session) {
        Cart cart = cartSessionService.getCart(session);

        return Mono.fromRunnable(() -> cart.decreaseQuantity(id))
                .then(redirect(redirectUrl));
    }

    @PostMapping("/remove/{id}")
    public Mono<Rendering> remove(@PathVariable Long id, @RequestParam String redirectUrl, WebSession session) {
        Cart cart = cartSessionService.getCart(session);

        return Mono.fromRunnable(() -> cart.removeItem(id))
                .then(redirect(redirectUrl));
    }

    @PostMapping("/clear")
    public Mono<Rendering> clear(@RequestParam String redirectUrl, WebSession session) {
        Cart cart = cartSessionService.getCart(session);

        return Mono.fromRunnable(cart::clearCart)
                .then(redirect(redirectUrl));
    }

    private Mono<Rendering> redirect(String url) {
        if (url == null || url.isBlank()) {
            return Mono.just(Rendering.redirectTo("/items").build());
        }

        return Mono.just(Rendering.redirectTo(url).build());
    }
}
