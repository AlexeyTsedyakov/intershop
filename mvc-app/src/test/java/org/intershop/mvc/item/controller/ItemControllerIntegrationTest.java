package org.intershop.mvc.item.controller;

import org.intershop.mvc.item.model.Item;
import org.intershop.mvc.item.repository.ItemRepository;
import org.intershop.mvc.order.repository.OrderItemRepository;
import org.intershop.mvc.shared.config.EmbeddedRedisConfig;
import org.intershop.mvc.shared.security.WithMockUserPrincipal;
import org.intershop.mvc.upload.service.FileUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@SpringBootTest
@AutoConfigureWebTestClient
@Import(EmbeddedRedisConfig.class)
@ActiveProfiles("test")
public class ItemControllerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @MockitoBean
    private FileUploadService fileUploadService;

    private Item testItem;

    @BeforeEach
    void setUp() {
        Item item = new Item(null, "name", "image", 100, "description");
        StepVerifier.create(orderItemRepository.deleteAll()
                        .then(itemRepository.deleteAll())
                        .then(itemRepository.save(item)))
                .assertNext(saved -> testItem = saved)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void showItems_shouldReturnView() {
        webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/items")
                        .queryParam("page", "0")
                        .queryParam("size", "3")
                        .queryParam("search", "test")
                        .queryParam("sort", "name")
                        .build())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithAnonymousUser
    void showItem_shouldReturnView() {
        long id = testItem.getId();

        webTestClient
                .get()
                .uri("/items/" + id)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUserPrincipal
    void showNewItem_shouldReturnView_withAuthenticatedUser() {
        webTestClient
                .get()
                .uri("/items/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithAnonymousUser
    void showNewItem_shouldRedirectToLogin_withAnonymousUser() {
        webTestClient
                .get()
                .uri("items/new")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/login");
    }

    @Test
    @WithMockUserPrincipal
    void createItem_shouldRedirect_withAuthenticatedUser() {
        when(fileUploadService.uploadImage(any())).thenReturn(Mono.just("uploaded_test.jpg"));

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("imageFile", "content".getBytes())
                .filename("test.jpg")
                .contentType(MediaType.IMAGE_JPEG);
        builder.part("name", "New Item");
        builder.part("price", "200");
        builder.part("description", "New Desc");

        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/items")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches(HttpHeaders.LOCATION, "/items/\\d+");
    }

    @Test
    @WithMockUserPrincipal
    void createItem_shouldReturnForbidden_withNoCsrf() {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("imageFile", "content".getBytes())
                .filename("test.jpg")
                .contentType(MediaType.IMAGE_JPEG);
        builder.part("name", "New Item");
        builder.part("price", "200");
        builder.part("description", "New Desc");

        webTestClient
                .post()
                .uri("/items")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUserPrincipal
    void createItem_shouldReturnView_whenInvalidForm() {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("imageFile", "content".getBytes())
                .filename("test.jpg")
                .contentType(MediaType.IMAGE_JPEG);
        builder.part("name", ""); // Invalid: blank
        builder.part("price", "0"); // Invalid: min 1

        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/items")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isOk();
    }
}
