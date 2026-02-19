package org.example.intershop.controller;

import org.example.intershop.model.Cart;
import org.example.intershop.service.CartService;
import org.example.intershop.service.CartSessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(CartController.class)
public class CartControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private Cart cart;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private CartSessionService cartSessionService;

    @Test
    void showCart_shouldReturnView() {
        long totalAmount = 155L;
        when(cartSessionService.getCart(any())).thenReturn(cart);
        when(cartService.fetchItems(any(Cart.class))).thenReturn(Mono.empty());
        when(cartService.getTotalAmount(any(Cart.class))).thenReturn(Mono.just(totalAmount));

        webTestClient.get().uri("/cart")
                .exchange()
                .expectStatus().isOk();

        verify(cartService, times(1)).fetchItems(any(Cart.class));
        verify(cartService, times(1)).getTotalAmount(any(Cart.class));
    }

    @Test
    void increase_shouldRedirect() {
        long id = 1L;
        String refererUrl = "/items";
        when(cartSessionService.getCart(any())).thenReturn(cart);

        webTestClient.post().uri("/cart/increase/" + id)
                        .header(HttpHeaders.REFERER, refererUrl)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, refererUrl);

        verify(cart, times(1)).increaseQuantity(id);
    }

    @Test
    void decrease_shouldRedirect() {
        long id = 1L;
        String refererUrl = "/items";
        when(cartSessionService.getCart(any())).thenReturn(cart);

        webTestClient.post().uri("/cart/decrease/" + id)
                        .header(HttpHeaders.REFERER, refererUrl)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, refererUrl);

        verify(cart, times(1)).decreaseQuantity(id);
    }

    @Test
    void remove_shouldRedirect() {
        long id = 1L;
        String refererUrl = "/items";
        when(cartSessionService.getCart(any())).thenReturn(cart);

        webTestClient.post().uri("/cart/remove/" + id)
                        .header(HttpHeaders.REFERER, refererUrl)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, refererUrl);

        verify(cart, times(1)).removeItem(id);
    }

    @Test
    void clear_shouldRedirect() {
        String refererUrl = "/items";
        when(cartSessionService.getCart(any())).thenReturn(cart);

        webTestClient.post().uri("/cart/clear")
                        .header(HttpHeaders.REFERER, refererUrl)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, refererUrl);

        verify(cart, times(1)).clearCart();
    }
}
