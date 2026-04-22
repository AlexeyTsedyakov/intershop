package org.intershop.mvc.order.controller;

import org.intershop.mvc.cart.model.Cart;
import org.intershop.mvc.order.model.Order;
import org.intershop.mvc.cart.service.CartSessionService;
import org.intershop.mvc.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.client.autoconfigure.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.security.oauth2.client.autoconfigure.reactive.ReactiveOAuth2ClientWebSecurityAutoConfiguration;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(
        controllers = OrderController.class,
        excludeAutoConfiguration = {
                ReactiveOAuth2ClientWebSecurityAutoConfiguration.class,
                ReactiveOAuth2ClientAutoConfiguration.class
        }
)
public class OrderControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private CartSessionService cartSessionService;

    @MockitoBean
    private Cart cart;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
    }

    @Test
    void showOrder_shouldReturnView() {
        Long id = testOrder.getId();
        when(orderService.findById(id)).thenReturn(Mono.just(testOrder));

        webTestClient.get().uri("/orders/" + id)
                .exchange()
                .expectStatus().isOk();

        verify(orderService, times(1)).findById(id);
    }

    @Test
    void showOrders_shouldReturnView() {
        List<Order> orders = List.of(testOrder);
        long ordersAmount = 155L;
        when(orderService.findAll()).thenReturn(Flux.fromIterable(orders));
        when(orderService.getOrdersAmount(orders)).thenReturn(Mono.just(ordersAmount));

        webTestClient.get().uri("/orders")
                .exchange()
                .expectStatus().isOk();

        verify(orderService, times(1)).findAll();
        verify(orderService, times(1)).getOrdersAmount(orders);
    }

    @Test
    void placeOrder_shouldRedirect() {
        when(cartSessionService.getCart(any())).thenReturn(cart);
        when(orderService.placeOrder(any(Cart.class))).thenReturn(Mono.just(testOrder));

        webTestClient.post().uri("/orders")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/orders/" + testOrder.getId());

        verify(orderService, times(1)).placeOrder(any(Cart.class));
    }
}
