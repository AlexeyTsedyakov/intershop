package org.example.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.example.intershop.model.Cart;
import org.example.intershop.model.Order;
import org.example.intershop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final Cart cart;

    @GetMapping("/{id}")
    public String showOrder(Model model, @PathVariable Long id) {
        model.addAttribute("order", orderService.findById(id));

        return "order";
    }

    @GetMapping
    public String showOrders(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        model.addAttribute("ordersAmount", orderService.getOrdersAmount(orders));

        return "orders";
    }

    @PostMapping
    public String placeOrder() {
        Order order = orderService.placeOrder(cart);

        return "redirect:/orders/" + order.getId();
    }
}
