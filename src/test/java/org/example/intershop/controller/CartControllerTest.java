package org.example.intershop.controller;

import org.example.intershop.model.Cart;
import org.example.intershop.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpHeaders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Cart cart;

    @MockitoBean
    private CartService cartService;

    @Test
    void showCart_shouldReturnView() throws Exception {
        long totalAmount = 155L;
        doNothing().when(cartService).fetchItems(any(Cart.class));
        when(cartService.getTotalAmount(any(Cart.class))).thenReturn(totalAmount);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("cart"))
                .andExpect(model().attribute("totalAmount", totalAmount));

        verify(cartService, times(1)).fetchItems(any(Cart.class));
        verify(cartService, times(1)).getTotalAmount(any(Cart.class));
    }

    @Test
    void increase_shouldRedirect() throws Exception {
        long id = 1L;
        String refererUrl = "/items";

        mockMvc.perform(post("/cart/increase/" + id)
                        .header(HttpHeaders.REFERER, refererUrl))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(refererUrl));

        verify(cart, times(1)).increaseQuantity(id);
    }

    @Test
    void decrease_shouldRedirect() throws Exception {
        long id = 1L;
        String refererUrl = "/items";

        mockMvc.perform(post("/cart/decrease/" + id)
                        .header(HttpHeaders.REFERER, refererUrl))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(refererUrl));

        verify(cart, times(1)).decreaseQuantity(id);
    }

    @Test
    void remove_shouldRedirect() throws Exception {
        long id = 1L;
        String refererUrl = "/items";

        mockMvc.perform(post("/cart/remove/" + id)
                        .header(HttpHeaders.REFERER, refererUrl))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(refererUrl));

        verify(cart, times(1)).removeItem(id);
    }

    @Test
    void clear_shouldRedirect() throws Exception {
        String refererUrl = "/items";
        doNothing().when(cart).clearCart();

        mockMvc.perform(post("/cart/clear")
                        .header(HttpHeaders.REFERER, refererUrl))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(refererUrl));

        verify(cart, times(1)).clearCart();
    }
}
