package org.example.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.example.intershop.model.Cart;
import org.example.intershop.service.CartService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartController {
    private final Cart cart;
    private final CartService cartService;

    @GetMapping
    public String showCart(Model model) {
        cartService.fetchItems(cart);
        long totalAmount = cartService.getTotalAmount(cart);

        model.addAttribute("cart", cart);
        model.addAttribute("totalAmount", totalAmount);

        return "cart";
    }

    @PostMapping("/increase/{id}")
    public String add(@PathVariable Long id, @RequestHeader(value = HttpHeaders.REFERER) String referer) {
        cart.increaseQuantity(id);
        return redirectToReferer(referer);
    }

    @PostMapping("/decrease/{id}")
    public String decrease(@PathVariable Long id, @RequestHeader(value = HttpHeaders.REFERER) String referer) {
        cart.decreaseQuantity(id);
        return redirectToReferer(referer);
    }

    @PostMapping("/remove/{id}")
    public String remove(@PathVariable Long id, @RequestHeader(value = HttpHeaders.REFERER) String referer) {
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
