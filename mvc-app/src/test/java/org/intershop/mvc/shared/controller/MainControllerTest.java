package org.intershop.mvc.shared.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.client.autoconfigure.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.security.oauth2.client.autoconfigure.reactive.ReactiveOAuth2ClientWebSecurityAutoConfiguration;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(
        controllers = MainController.class,
        excludeAutoConfiguration = {
                ReactiveOAuth2ClientWebSecurityAutoConfiguration.class,
                ReactiveOAuth2ClientAutoConfiguration.class
        }
)
public class MainControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void showMain_shouldRedirectToItems() {
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/items");
    }
}
