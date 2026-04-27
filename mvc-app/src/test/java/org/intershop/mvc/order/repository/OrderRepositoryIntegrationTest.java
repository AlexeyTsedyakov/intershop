package org.intershop.mvc.order.repository;

import org.intershop.mvc.shared.config.JpaAuditingConfig;
import org.intershop.mvc.order.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DataR2dbcTest
@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
public class OrderRepositoryIntegrationTest {
    @Autowired
    private OrderRepository orderRepository;

    private Order savedOrder;

    @BeforeEach
    void setUp() {
        Order order = new Order();
        order.setTotalAmount(0L);
        order.setUserId(1L);

        StepVerifier.create(orderRepository.deleteAll().then(orderRepository.save(order)))
                .assertNext(saved -> savedOrder = saved)
                .verifyComplete();
    }

    @Test
    void save_shouldPersist_withNewOrder() {
        Order order = new Order();
        order.setTotalAmount(100L);
        order.setUserId(1L);

        StepVerifier.create(orderRepository.save(order))
                .assertNext(saved -> {
                    assertThat(saved).isNotNull();
                    assertThat(saved)
                            .usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(order);
                })
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnNotEmpty_withCorrectId() {
        long id = savedOrder.getId();

        StepVerifier.create(orderRepository.findById(id))
                .assertNext(found -> {
                    assertThat(found)
                            .usingRecursiveComparison()
                            .ignoringFields("createdAt")
                            .isEqualTo(savedOrder);
                    assertThat(found.getCreatedAt())
                            .isCloseTo(savedOrder.getCreatedAt(), within(1, ChronoUnit.MILLIS)); // H2 режет наносекунды
                })
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnEmpty_withWrongId() {
        long id = -1L;

        StepVerifier.create(orderRepository.findById(id))
                .verifyComplete();
    }

    @Test
    void save_shouldUpdateOrder_withExistentOrder() {
        long id = savedOrder.getId();
        long totalAmount = 150;

        Mono<Order> updatedMono = orderRepository.findById(id)
                .doOnNext(order -> order.setTotalAmount(totalAmount))
                .flatMap(orderRepository::save);

        StepVerifier.create(updatedMono)
                .assertNext(updated -> {
                    assertThat(updated).isNotNull();
                    assertThat(updated.getId()).isEqualTo(id);
                    assertThat(updated.getTotalAmount()).isEqualTo(totalAmount);
                })
                .verifyComplete();
    }

    @Test
    void deleteById_shouldDeleteOrder() {
        long id = savedOrder.getId();

        StepVerifier.create(orderRepository.deleteById(id))
                .verifyComplete();

        StepVerifier.create(orderRepository.findById(id))
                .verifyComplete();
    }
}

