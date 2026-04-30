package org.intershop.mvc.cart.controller;

import org.intershop.mvc.shared.config.EmbeddedRedisConfig;
import org.intershop.mvc.shared.security.WithMockUserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@SpringBootTest
@Import(EmbeddedRedisConfig.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class CartControllerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    @WithMockUserPrincipal
    void showCart_shouldReturnView_withAuthenticatedUser() {
        webTestClient
                .get()
                .uri("/cart")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithAnonymousUser
    void showCart_shouldRedirectToLogin_withAnonymousUser() {
        webTestClient
                .get()
                .uri("/cart")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/login");
    }

    @Test
    @WithMockUserPrincipal
    void increase_shouldRedirect_withCsrf() {
        long id = 1L;
        String redirectUrl = "/items";

        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/cart/increase/" + id + "?redirectUrl=" + redirectUrl)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, redirectUrl);
    }

    @Test
    @WithMockUserPrincipal
    void increase_shouldReturnForbidden_withNoCsrf() {
        long id = 1L;
        String redirectUrl = "/items";

        webTestClient
                .post()
                .uri("/cart/increase/" + id + "?redirectUrl=" + redirectUrl)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUserPrincipal
    void decrease_shouldRedirect() {
        long id = 1L;
        String redirectUrl = "/items";

        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/cart/decrease/" + id + "?redirectUrl=" + redirectUrl)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, redirectUrl);
    }

    @Test
    @WithMockUserPrincipal
    void remove_shouldRedirect() {
        long id = 1L;
        String redirectUrl = "/items";

        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/cart/remove/" + id + "?redirectUrl=" + redirectUrl)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, redirectUrl);
    }

    @Test
    @WithMockUserPrincipal
    void clear_shouldRedirect() {
        String redirectUrl = "/items";

        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/cart/clear?redirectUrl=" + redirectUrl)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, redirectUrl);
    }
}
