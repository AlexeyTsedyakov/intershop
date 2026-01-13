package org.example.intershop.repository;

import org.example.intershop.config.JpaAuditingConfig;
import org.example.intershop.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuditingConfig.class)
public class OrderRepositoryIntegrationTest {
    @Autowired
    private OrderRepository orderRepository;

    private Order savedOrder;

    @BeforeEach
    void setUp() {
        Order order = new Order();
        order.setTotalAmount(0L);
        savedOrder = orderRepository.saveAndFlush(order);
    }

    @Test
    void save_shouldPersist_withNewOrder() {
        Order order = new Order();
        order.setTotalAmount(100L);

        Order savedOrder = orderRepository.saveAndFlush(order);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(order);
    }

    @Test
    void findById_shouldReturnNotEmptyOptional_withCorrectId() {
        long id = savedOrder.getId();

        Optional<Order> order = orderRepository.findById(id);

        assertThat(order)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(savedOrder);
    }

    @Test
    void findById_shouldReturnEmptyOptional_withWrongId() {
        long id = -1L;

        Optional<Order> order = orderRepository.findById(id);

        assertThat(order).isEmpty();
    }

    @Test
    void save_shouldUpdateOrder_withExistentOrder() {
        long id = savedOrder.getId();
        long totalAmount = 150;

        Order order = orderRepository.findById(id).orElse(null);
        assertThat(order).isNotNull();
        assertThat(order.getId()).isEqualTo(id);

        order.setTotalAmount(totalAmount);
        Order updatedOrder = orderRepository.save(order);
        assertThat(updatedOrder).isNotNull();
        assertThat(updatedOrder.getId()).isEqualTo(id);
        assertThat(updatedOrder.getTotalAmount()).isEqualTo(totalAmount);
    }

    @Test
    void deleteById_shouldDeleteOrder() {
        long id = savedOrder.getId();

        orderRepository.deleteById(id);
        Optional<Order> deletedOrder = orderRepository.findById(id);

        assertThat(deletedOrder).isEmpty();
    }
}
