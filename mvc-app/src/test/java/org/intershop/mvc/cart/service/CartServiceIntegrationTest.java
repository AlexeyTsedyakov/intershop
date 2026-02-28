package org.intershop.mvc.cart.service;

import org.intershop.mvc.item.service.ItemService;
import org.intershop.mvc.cart.model.Cart;
import org.intershop.mvc.cart.model.CartItem;
import org.intershop.mvc.item.model.Item;
import org.intershop.mvc.shared.config.EmbeddedRedisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(EmbeddedRedisConfig.class)
@ActiveProfiles("test")
public class CartServiceIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private ItemService itemService;

    private Item savedItem1;
    private Item savedItem2;

    @BeforeEach
    void setUp() {
        Item item1 = new Item(null, "Item 1", "img1", 100, "desc1");
        Item item2 = new Item(null, "Item 2", "img2", 200, "desc2");

        StepVerifier.create(itemService.save(item1))
                .assertNext(saved -> savedItem1 = saved)
                .verifyComplete();
        StepVerifier.create(itemService.save(item2))
                .assertNext(saved -> savedItem2 = saved)
                .verifyComplete();
    }

    @Test
    void fetchItems_shouldPopulateCartItems_whenItemsExist() {
        Cart cart = new Cart();
        cart.increaseQuantity(savedItem1.getId());
        cart.increaseQuantity(savedItem2.getId());

        StepVerifier.create(cartService.fetchItems(cart))
                .verifyComplete();

        List<CartItem> items = cart.getItems();
        assertThat(items).hasSize(2);

        CartItem cartItem1 = cart.getItemById(savedItem1.getId());
        assertThat(cartItem1.getItem()).isNotNull();
        assertThat(cartItem1.getItem().getId()).isEqualTo(savedItem1.getId());
        assertThat(cartItem1.getPrice()).isEqualTo(savedItem1.getPrice());

        CartItem cartItem2 = cart.getItemById(savedItem2.getId());
        assertThat(cartItem2.getItem()).isNotNull();
        assertThat(cartItem2.getItem().getId()).isEqualTo(savedItem2.getId());
        assertThat(cartItem2.getPrice()).isEqualTo(savedItem2.getPrice());
    }

    @Test
    void fetchItems_shouldThrowException_whenItemNotFound() {
        Cart cart = new Cart();
        cart.increaseQuantity(999999L);

        StepVerifier.create(cartService.fetchItems(cart))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && "Some items were not found".equals(throwable.getMessage()))
                .verify();
    }

    @Test
    void getTotalAmount_shouldCalculateCorrectly_whenItemsAreNotFetched() {
        Cart cart = new Cart();
        cart.increaseQuantity(savedItem1.getId());
        cart.increaseQuantity(savedItem1.getId()); // quantity = 2
        cart.increaseQuantity(savedItem2.getId()); // quantity = 1
        long expected = savedItem1.getPrice() * 2 + savedItem2.getPrice();

        StepVerifier.create(cartService.getTotalAmount(cart))
                .expectNext(expected)
                .verifyComplete();

        assertThat(cart.getItemById(savedItem1.getId()).getItem()).isNotNull();
    }
}
