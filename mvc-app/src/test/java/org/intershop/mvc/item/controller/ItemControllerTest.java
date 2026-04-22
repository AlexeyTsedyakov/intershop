package org.intershop.mvc.item.controller;

import org.intershop.mvc.cart.model.Cart;
import org.intershop.mvc.item.model.Item;
import org.intershop.mvc.cart.service.CartSessionService;
import org.intershop.mvc.upload.service.FileUploadService;
import org.intershop.mvc.item.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.client.autoconfigure.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.security.oauth2.client.autoconfigure.reactive.ReactiveOAuth2ClientWebSecurityAutoConfiguration;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(
        controllers = ItemController.class,
        excludeAutoConfiguration = {
                ReactiveOAuth2ClientWebSecurityAutoConfiguration.class,
                ReactiveOAuth2ClientAutoConfiguration.class
        }
)
public class ItemControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private CartSessionService cartSessionService;

    @MockitoBean
    private FileUploadService fileUploadService;

    private Item testItem;

    @BeforeEach
    void setUp() {
        testItem = new Item(1L, "Test Item", "image.jpg", 100, "Description");
        when(cartSessionService.getCart(any())).thenReturn(new Cart());
    }

    @Test
    void showItems_shouldReturnView() {
        Page<Item> testPage = new PageImpl<>(List.of(testItem));
        when(itemService.findAll(any(), any(Pageable.class))).thenReturn(Mono.just(testPage));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/items")
                        .queryParam("page", "0")
                        .queryParam("size", "3")
                        .queryParam("search", "test")
                        .queryParam("sort", "name")
                        .build())
                .exchange()
                .expectStatus().isOk();

        verify(itemService, times(1)).findAll(any(), any(Pageable.class));
    }

    @Test
    void showItem_shouldReturnView() {
        long id = testItem.getId();
        when(itemService.findById(id)).thenReturn(Mono.just(testItem));

        webTestClient.get()
                .uri("/items/" + id)
                .exchange()
                .expectStatus().isOk();

        verify(itemService, times(1)).findById(id);
    }

    @Test
    void showNewItem_shouldReturnView() {
        webTestClient.get()
                .uri("/items/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void createItem_shouldRedirect_whenValid() {
        long id = testItem.getId();
        when(fileUploadService.uploadImage(any())).thenReturn(Mono.just("uploaded_test.jpg"));
        when(itemService.save(any(Item.class))).thenReturn(Mono.just(testItem));

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("imageFile", "content".getBytes()).filename("test.jpg");
        builder.part("name", "New Item");
        builder.part("price", "200");
        builder.part("description", "New Desc");

        webTestClient.post()
                .uri("/items")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/items/" + id);

        verify(fileUploadService).uploadImage(any());
        verify(itemService).save(any(Item.class));
    }

    @Test
    void createItem_shouldReturnView_whenInvalid() {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("imageFile", "content".getBytes()).filename("test.jpg");
        builder.part("name", ""); // Invalid: blank
        builder.part("price", "0"); // Invalid: min 1

        webTestClient.post()
                .uri("/items")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isOk();

        verify(itemService, never()).save(any(Item.class));
    }
}
