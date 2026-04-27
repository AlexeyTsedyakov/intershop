package org.intershop.mvc.order.controller;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.cart.model.Cart;
import org.intershop.mvc.cart.service.CartSessionService;
import org.intershop.mvc.order.service.OrderService;
import org.intershop.mvc.shared.util.AuthUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final CartSessionService cartSessionService;
    private final AuthUtil authUtil;

    @GetMapping("/{id}")
    public Mono<Rendering> showOrder(@PathVariable Long id, Authentication authentication) {
        return orderService.findById(id)
                .map(order -> Rendering.view("order")
                        .modelAttribute("order", order)
                        .modelAttribute("isAuthenticated", authUtil.isAuthenticated(authentication))
                        .build());
    }

    @GetMapping
    public Mono<Rendering> showOrders(Authentication authentication) {
        return orderService.findAllByUserId(authUtil.getUserId(authentication))
                .collectList()
                .flatMap(orders -> orderService.getOrdersAmount(orders)
                        .map(ordersAmount -> Rendering.view("orders")
                                .modelAttribute("orders", orders)
                                .modelAttribute("ordersAmount", ordersAmount)
                                .modelAttribute("isAuthenticated", authUtil.isAuthenticated(authentication))
                                .build()));
    }

    @PostMapping
    public Mono<Rendering> placeOrder(WebSession session, Authentication authentication) {
        Cart cart = cartSessionService.getCart(session);
        cart.setUserId(authUtil.getUserId(authentication));

        return orderService.placeOrder(cart)
                .map(order -> Rendering.redirectTo("/orders/" + order.getId()).build());
    }
}
