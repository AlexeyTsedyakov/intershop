package org.example.intershop.service;

import org.example.intershop.model.Cart;
import org.example.intershop.model.Item;
import org.example.intershop.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CartService cartService;

    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        item1 = itemService.save(new Item(null, "Item 1", "img1", 100, "desc1"));
        item2 = itemService.save(new Item(null, "Item 2", "img2", 200, "desc2"));
    }

    @Test
    void placeOrder_shouldCreateOrder() {
        Cart cart = new Cart();
        cart.increaseQuantity(item1.getId());
        cart.increaseQuantity(item1.getId()); // 2 * item1
        cart.increaseQuantity(item2.getId()); // 1 * item2
        
        cartService.fetchItems(cart);

        Order order = orderService.placeOrder(cart);
        long expectedTotal = cart.calculateTotalAmount();
        int expectedSize = cart.getItems().size();

        assertThat(order).isNotNull();
        assertThat(order.getId()).isNotNull();
        assertThat(order.getTotalAmount()).isEqualTo(expectedTotal);
        assertThat(order.getItems()).hasSize(expectedSize);

        Order savedOrder = orderService.findById(order.getId());
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getTotalAmount()).isEqualTo(expectedTotal);
        assertThat(savedOrder.getItems()).hasSize(expectedSize);
    }

    @Test
    void findById_shouldReturnOrder() {
        Cart cart = new Cart();
        cart.increaseQuantity(item1.getId());
        cartService.fetchItems(cart);
        Order order = orderService.placeOrder(cart);

        Order foundOrder = orderService.findById(order.getId());

        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getId()).isEqualTo(order.getId());
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> orderService.findById(-1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findAll_shouldReturnList() {
        Cart cart1 = new Cart();
        cart1.increaseQuantity(item1.getId());
        cartService.fetchItems(cart1);
        Order order1 = orderService.placeOrder(cart1);

        Cart cart2 = new Cart();
        cart2.increaseQuantity(item2.getId());
        cartService.fetchItems(cart2);
        Order order2 = orderService.placeOrder(cart2);

        List<Order> orders = orderService.findAll();

        assertThat(orders).isNotEmpty();
        assertThat(orders).extracting(Order::getId).contains(order1.getId(), order2.getId());
    }

    @Test
    void getOrdersAmount_shouldCalculateCorrectly() {
        Cart cart1 = new Cart();
        cart1.increaseQuantity(item1.getId()); // item1
        cartService.fetchItems(cart1);
        Order order1 = orderService.placeOrder(cart1);

        Cart cart2 = new Cart();
        cart2.increaseQuantity(item2.getId()); // item2
        cartService.fetchItems(cart2);
        Order order2 = orderService.placeOrder(cart2);

        long amount = orderService.getOrdersAmount(List.of(order1, order2));
        long expectedAmount = cart1.calculateTotalAmount() + cart2.calculateTotalAmount();

        assertThat(amount).isEqualTo(expectedAmount);
    }
}
