package org.example.intershop.service;

import org.example.intershop.model.Cart;
import org.example.intershop.model.CartItem;
import org.example.intershop.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CartServiceIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private ItemService itemService;

    private Item savedItem1;
    private Item savedItem2;

    @BeforeEach
    void setUp() {
        savedItem1 = itemService.save(new Item(null, "Item 1", "img1", 100, "desc1"));
        savedItem2 = itemService.save(new Item(null, "Item 2", "img2", 200, "desc2"));
    }

    @Test
    void fetchItems_shouldPopulateCartItems_whenItemsExist() {
        Cart cart = new Cart();
        cart.increaseQuantity(savedItem1.getId());
        cart.increaseQuantity(savedItem2.getId());

        cartService.fetchItems(cart);

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

        assertThatThrownBy(() -> cartService.fetchItems(cart))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Some items were not found");
    }

    @Test
    void getTotalAmount_shouldCalculateCorrectly_whenItemsAreNotFetched() {
        Cart cart = new Cart();
        cart.increaseQuantity(savedItem1.getId());
        cart.increaseQuantity(savedItem1.getId()); // quantity = 2
        cart.increaseQuantity(savedItem2.getId()); // quantity = 1
        long expected = savedItem1.getPrice() * 2 + savedItem2.getPrice();

        long total = cartService.getTotalAmount(cart);

        assertThat(total).isEqualTo(expected);
        
        assertThat(cart.getItemById(savedItem1.getId()).getItem()).isNotNull();
    }
}
