package org.example.intershop.service;

import org.example.intershop.model.Cart;
import org.example.intershop.model.Item;
import org.example.intershop.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
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
        item1 = itemService.save(new Item(null, "Item 1", "img1", 100, "desc1")).block();
        item2 = itemService.save(new Item(null, "Item 2", "img2", 200, "desc2")).block();
    }

    @Test
    void placeOrder_shouldCreateOrder() {
        Cart cart = new Cart();
        cart.increaseQuantity(item1.getId());
        cart.increaseQuantity(item1.getId()); // 2 * item1
        cart.increaseQuantity(item2.getId()); // 1 * item2

        StepVerifier.create(cartService.fetchItems(cart)
                        .then(orderService.placeOrder(cart)))
                .assertNext(order -> {
                    long expectedTotal = order.calculateTotalAmount();
                    int expectedSize = order.getItems().size();

                    assertThat(order).isNotNull();
                    assertThat(order.getId()).isNotNull();
                    assertThat(order.getTotalAmount()).isEqualTo(expectedTotal);
                    assertThat(order.getItems()).hasSize(expectedSize);
                    assertThat(cart.getItems()).isEmpty();
                })
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnOrder() {
        Cart cart = new Cart();
        cart.increaseQuantity(item1.getId());

        StepVerifier.create(cartService.fetchItems(cart)
                        .then(orderService.placeOrder(cart))
                        .flatMap(savedOrder -> orderService.findById(savedOrder.getId())
                                .zipWith(Mono.just(savedOrder))))
                .assertNext(tuple -> {
                    Order foundOrder = tuple.getT1();
                    Order originalOrder = tuple.getT2();

                    assertThat(foundOrder).isNotNull();
                    assertThat(foundOrder.getId()).isEqualTo(originalOrder.getId());
                })
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        StepVerifier.create(orderService.findById(-1L))
                .verifyComplete();
    }

    @Test
    void findAll_shouldReturnList() {
        Cart cart1 = new Cart();
        cart1.increaseQuantity(item1.getId());

        Cart cart2 = new Cart();
        cart2.increaseQuantity(item2.getId());

        StepVerifier.create(cartService.fetchItems(cart1)
                        .then(orderService.placeOrder(cart1))
                        .zipWith(cartService.fetchItems(cart2)
                                .then(orderService.placeOrder(cart2)))
                        .flatMapMany(tuple -> orderService.findAll()
                                .collectList()
                                .map(orders -> {
                                    List<Long> orderIds = orders.stream().map(Order::getId).toList();
                                    return orderIds.contains(tuple.getT1().getId()) &&
                                           orderIds.contains(tuple.getT2().getId());
                                })))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void getOrdersAmount_shouldCalculateCorrectly() {
        Cart cart1 = new Cart();
        cart1.increaseQuantity(item1.getId());

        Cart cart2 = new Cart();
        cart2.increaseQuantity(item2.getId());

        StepVerifier.create(
                        cartService.fetchItems(cart1).then(orderService.placeOrder(cart1))
                                .zipWith(cartService.fetchItems(cart2).then(orderService.placeOrder(cart2)))
                                .flatMap(tuple -> {
                                    Order order1 = tuple.getT1();
                                    Order order2 = tuple.getT2();
                                    long expectedAmount = order1.calculateTotalAmount() + order2.calculateTotalAmount();

                                    return orderService.getOrdersAmount(List.of(order1, order2))
                                            .map(amount -> amount == expectedAmount);
                                })
                )
                .expectNext(true)
                .verifyComplete();
    }
}
