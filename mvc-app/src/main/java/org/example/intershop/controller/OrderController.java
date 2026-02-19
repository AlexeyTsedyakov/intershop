package org.example.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.example.intershop.model.Cart;
import org.example.intershop.service.CartSessionService;
import org.example.intershop.service.OrderService;
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

    @GetMapping("/{id}")
    public Mono<Rendering> showOrder(@PathVariable Long id) {
        return orderService.findById(id)
                .map(order -> Rendering.view("order")
                        .modelAttribute("order", order)
                        .build());
    }

    @GetMapping
    public Mono<Rendering> showOrders() {
        return orderService.findAll()
                .collectList()
                .flatMap(orders -> orderService.getOrdersAmount(orders)
                        .map(ordersAmount -> Rendering.view("orders")
                                .modelAttribute("orders", orders)
                                .modelAttribute("ordersAmount", ordersAmount)
                                .build()));
    }

    @PostMapping
    public Mono<Rendering> placeOrder(WebSession session) {
        Cart cart = cartSessionService.getCart(session);

        return orderService.placeOrder(cart)
                .map(order -> Rendering.redirectTo("/orders/" + order.getId()).build());
    }
}
