package org.intershop.mvc.payment.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

@SpringBootTest
public class PaymentServiceIntegrationTest {
    @Autowired
    private PaymentService paymentService;

    @Test
    void processPayment_shouldNotThrowError_whenBalanceEnough() {
        long totalAmount = 0;

        StepVerifier.create(paymentService.processPayment(totalAmount))
                .verifyComplete();
    }

    @Test
    void processPayment_shouldThrowError_whenBalanceNotEnough() {
        long totalAmount = Long.MAX_VALUE;

        StepVerifier.create(paymentService.processPayment(totalAmount))
                .verifyError(WebClientResponseException.class);
    }
}
