package org.intershop.mvc.order.service;

import org.intershop.mvc.cart.model.Cart;
import org.intershop.mvc.order.factory.OrderFactory;
import org.intershop.mvc.order.model.Order;
import org.intershop.mvc.order.repository.OrderRepository;
import org.intershop.mvc.payment.service.PaymentService;
import org.intershop.mvc.shared.util.AuthUtil;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = OrderServiceImpl.class)
public class OrderServiceImplTest {
    @Autowired
    private OrderServiceImpl orderService;
    @MockitoBean
    private OrderRepository orderRepository;
    @MockitoBean
    private OrderItemService orderItemService;
    @MockitoBean
    private PaymentService paymentService;
    @MockitoBean
    private OrderFactory orderFactory;
    @MockitoBean
    private AuthUtil authUtil;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
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
    void findAllByUserId_shouldReturnList() {
        Long userId = 1L;
        List<Order> expected = List.of(testOrder);
        when(orderRepository.findAllByUserId(userId)).thenReturn(Flux.fromIterable(expected));
        when(orderItemService.findAllByOrderIdsAsMap(any())).thenReturn(Mono.just(Map.of()));

        Flux<Order> orders = orderService.findAllByUserId(userId);

        StepVerifier.create(orders)
                .expectNext(testOrder)
                .verifyComplete();

        verify(orderRepository).findAllByUserId(userId);
        verify(orderItemService).findAllByOrderIdsAsMap(any());
    }

    @Test
    void placeOrder_shouldCreateAndSaveOrder_withItemsAndTotalAmount() {
        long userId = 1;
        Cart cart = new Cart();
        cart.setUserId(userId);
        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(123L);
        order.setUserId(userId);

        when(orderFactory.createFromCart(any(Cart.class))).thenReturn(order);
        when(paymentService.processPayment(anyLong(), anyLong())).thenReturn(Mono.empty());
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(order));
        when(orderItemService.saveAll(anyList())).thenReturn(Flux.empty());

        StepVerifier.create(orderService.placeOrder(cart))
                .expectNext(order)
                .verifyComplete();

        verify(orderFactory, times(1)).createFromCart(cart);
        verify(paymentService, times(1)).processPayment(
                order.getUserId(), order.getTotalAmount());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemService, times(1)).saveAll(any());
        assertThat(cart.getItems()).isEmpty();
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
}
