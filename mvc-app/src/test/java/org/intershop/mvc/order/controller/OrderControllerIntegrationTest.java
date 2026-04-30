package org.intershop.mvc.order.controller;

import org.intershop.mvc.order.model.Order;
import org.intershop.mvc.order.repository.OrderRepository;
import org.intershop.mvc.shared.security.WithMockUserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class OrderControllerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private OrderRepository orderRepository;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        Order order = new Order();
        order.setTotalAmount(0L);
        order.setUserId(1L);

        StepVerifier.create(orderRepository.deleteAll().then(orderRepository.save(order)))
                .assertNext(saved -> testOrder = saved)
                .verifyComplete();
    }

    @Test
    @WithMockUserPrincipal
    void showOrder_shouldReturnView_withAuthenticatedUser() {
        Long id = testOrder.getId();

        webTestClient
                .get()
                .uri("/orders/" + id)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithAnonymousUser
    void showOrder_shouldRedirectToLogin_withAnonymousUser() {
        Long id = testOrder.getId();

        webTestClient
                .get()
                .uri("/orders/" + id)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/login");
    }

    @Test
    @WithMockUserPrincipal
    void showOrders_shouldReturnView() {
        webTestClient
                .get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUserPrincipal
    void placeOrder_shouldRedirectToNewOrder_withAuthenticatedUser() {
        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/orders")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches(HttpHeaders.LOCATION, "/orders/\\d+");
    }

    @Test
    @WithAnonymousUser
    void placeOrder_shouldReturnForbidden_withNoCsrf() {
        webTestClient
                .post()
                .uri("/orders")
                .exchange()
                .expectStatus().isForbidden();
    }
}
