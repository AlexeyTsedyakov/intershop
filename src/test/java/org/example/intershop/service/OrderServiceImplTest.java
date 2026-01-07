package org.example.intershop.service;

import org.example.intershop.model.*;
import org.example.intershop.repository.OrderRepository;
import org.example.intershop.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = OrderServiceImpl.class)
public class OrderServiceImplTest {
    @MockitoBean
    private OrderRepository orderRepository;

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
        when(orderRepository.findById(id)).thenReturn(Optional.of(testOrder));

        Order order = orderService.findById(id);

        assertThat(order).isSameAs(testOrder);
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    void findById_shouldThrowException_forNonExistentId() {
        long id = -1L;
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.findById(id)).isInstanceOf(NoSuchElementException.class);
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    void findAll_shouldReturnList() {
        List<Order> expected = List.of(testOrder);
        when(orderRepository.findAll()).thenReturn(expected);

        List<Order> orders = orderService.findAll();

        assertThat(orders).isEqualTo(expected);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void placeOrder_shouldCreateAndSaveOrder_withItemsAndTotalAmount() {
        Cart cart = new Cart();
        addToCart(cart, item1, 2);
        addToCart(cart, item2, 1);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        Order placedOrder = orderService.placeOrder(cart);

        verify(orderRepository).save(orderCaptor.capture());
        assertThat(orderCaptor.getValue()).isSameAs(placedOrder);

        assertThat(placedOrder.getItems()).hasSize(2);
        assertThat(placedOrder.getTotalAmount()).isEqualTo(cart.calculateTotalAmount());

        assertOrderItem(placedOrder, item1, 2);
        assertOrderItem(placedOrder, item2, 1);
    }

    @Test
    void placeOrder_withEmptyCart_shouldCreateAndSaveOrder_withZeroTotalAmount() {
        Cart cart = new Cart();

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        Order placedOrder = orderService.placeOrder(cart);

        verify(orderRepository).save(orderCaptor.capture());
        assertThat(orderCaptor.getValue()).isSameAs(placedOrder);
        assertThat(placedOrder.getItems()).isEmpty();
        assertThat(placedOrder.getTotalAmount()).isEqualTo(0L);
    }

    @Test
    void getOrdersAmount_shouldReturn0_forEmptyList() {
        assertThat(orderService.getOrdersAmount(List.of())).isEqualTo(0L);
    }

    @Test
    void getOrdersAmount_shouldSumTotalAmounts() {
        Order order1 = new Order();
        Order order2 = new Order();
        order1.setTotalAmount(100L);
        order2.setTotalAmount(250L);

        long amount = orderService.getOrdersAmount(List.of(order1, order2));

        assertThat(amount).isEqualTo(350L);
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
        assertThat(orderItem.getOrder()).isSameAs(order);
    }
}
