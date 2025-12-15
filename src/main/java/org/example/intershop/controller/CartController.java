package org.example.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.example.intershop.model.Cart;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartController {
    private final Cart cart;

    @PostMapping("/{id}/increase")
    public String add(@PathVariable Long id, @RequestHeader(value = HttpHeaders.REFERER) String referer) {
        cart.increaseQuantity(id);
        return redirectToReferer(referer);
    }

    @PostMapping("/{id}/decrease")
    public String remove(@PathVariable Long id, @RequestHeader(value = HttpHeaders.REFERER) String referer) {
        cart.decreaseQuantity(id);
        return redirectToReferer(referer);
    }

    @PostMapping("/{id}/remove")
    public String delete(@PathVariable Long id, @RequestHeader(value = HttpHeaders.REFERER) String referer) {
        cart.removeItem(id);
        return redirectToReferer(referer);
    }

    @PostMapping("/clear")
    public String clear(@RequestParam @RequestHeader(value = HttpHeaders.REFERER) String referer) {
        cart.clearCart();
        return redirectToReferer(referer);
    }

    private String redirectToReferer(String referer) {
        if (referer == null || referer.isBlank()) {
            return "redirect:/items"; // Если заголовка нет, возвращаемся на главную витрину
        }
        return "redirect:" + referer;
    }
}
