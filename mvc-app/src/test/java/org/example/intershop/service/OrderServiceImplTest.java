package org.example.intershop.service;

import org.example.intershop.model.*;
import org.example.intershop.repository.OrderRepository;
import org.example.intershop.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OrderServiceImpl.class)
public class OrderServiceImplTest {
    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private OrderItemService orderItemService;

    @Autowired
    private OrderServiceImpl orderService;

    private Order testOrder;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);

        item1 = new Item(1L, "name1", "image1", 100, "desc1");
        item2 = new Item(2L, "name2", "image2", 150, "desc2");
    }

    @Test
    void findById_shouldFindOrder_forExistingId() {
        long id = testOrder.getId();
        when(orderRepository.findById(id)).thenReturn(Mono.just(testOrder));
        when(orderItemService.findAllByOrderId(id)).thenReturn(Flux.empty());

        Mono<Order> result = orderService.findById(id);

        StepVerifier.create(result)
                .expectNext(testOrder)
                .verifyComplete();

        verify(orderRepository).findById(id);
        verify(orderItemService).findAllByOrderId(id);
    }

    @Test
    void findById_shouldReturnEmpty_forNonExistentId() {
        long id = -1L;
        when(orderRepository.findById(id)).thenReturn(Mono.empty());
        when(orderItemService.findAllByOrderId(id)).thenReturn(Flux.empty());

        Mono<Order> result = orderService.findById(id);

        StepVerifier.create(result)
                .verifyComplete();

        verify(orderRepository).findById(id);
    }

    @Test
    void findAll_shouldReturnList() {
        List<Order> expected = List.of(testOrder);
        when(orderRepository.findAll()).thenReturn(Flux.fromIterable(expected));
        when(orderItemService.findAllByOrderIdsAsMap(any())).thenReturn(Mono.just(Map.of()));

        Flux<Order> orders = orderService.findAll();

        StepVerifier.create(orders)
                .expectNext(testOrder)
                .verifyComplete();

        verify(orderRepository).findAll();
        verify(orderItemService).findAllByOrderIdsAsMap(any());
    }

    @Test
    void placeOrder_shouldCreateAndSaveOrder_withItemsAndTotalAmount() {
        Cart cart = new Cart();
        addToCart(cart, item1, 2);
        addToCart(cart, item2, 1);
        long expectedTotal = cart.calculateTotalAmount();

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(1L);
            return Mono.just(o);
        });
        when(orderItemService.saveAll(any())).thenAnswer(invocation -> {
            Iterable<OrderItem> items = invocation.getArgument(0);
            return Flux.fromIterable(items);
        });

        Mono<Order> result = orderService.placeOrder(cart);

        StepVerifier.create(result)
                .assertNext(placedOrder -> {
                    assertThat(placedOrder.getTotalAmount()).isEqualTo(expectedTotal);
                    assertThat(placedOrder.getItems()).hasSize(2);
                    assertOrderItem(placedOrder, item1, 2);
                    assertOrderItem(placedOrder, item2, 1);
                })
                .verifyComplete();

        verify(orderRepository).save(any(Order.class));
        verify(orderItemService).saveAll(any());
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    void placeOrder_withEmptyCart_shouldCreateAndSaveOrder_withZeroTotalAmount() {
        Cart cart = new Cart();

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            return Mono.just(o);
        });
        when(orderItemService.saveAll(any())).thenReturn(Flux.empty());

        Mono<Order> result = orderService.placeOrder(cart);

        StepVerifier.create(result)
                .assertNext(placedOrder -> {
                    assertThat(placedOrder.getItems()).isEmpty();
                    assertThat(placedOrder.getTotalAmount()).isEqualTo(0L);
                })
                .verifyComplete();

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrdersAmount_shouldReturn0_forEmptyList() {
        StepVerifier.create(orderService.getOrdersAmount(List.of()))
                .expectNext(0L)
                .verifyComplete();
    }

    @Test
    void getOrdersAmount_shouldSumTotalAmounts() {
        Order order1 = new Order();
        Order order2 = new Order();
        order1.setTotalAmount(100L);
        order2.setTotalAmount(250L);

        StepVerifier.create(orderService.getOrdersAmount(List.of(order1, order2)))
                .expectNext(350L)
                .verifyComplete();
    }

    private void addToCart(Cart cart, Item item, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cart.increaseQuantity(item.getId());
        }
        CartItem cartItem = cart.getItemById(item.getId());
        cartItem.setItem(item);
        cartItem.setPrice(item.getPrice());
    }

    private void assertOrderItem(Order order, Item expectedItem, int expectedQuantity) {
        OrderItem orderItem = order.getItemByItemId(expectedItem.getId());
        assertThat(orderItem)
                .as("OrderItem for itemId=%s", expectedItem.getId())
                .isNotNull();
        assertThat(orderItem.getItem()).isEqualTo(expectedItem);
        assertThat(orderItem.getQuantity()).isEqualTo(expectedQuantity);
        assertThat(orderItem.getPrice()).isEqualTo(expectedItem.getPrice());
        assertThat(orderItem.getOrderId()).isEqualTo(order.getId());
    }
}
