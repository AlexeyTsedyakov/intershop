package org.intershop.mvc.payment.service;

import org.intershop.mvc.payment.client.api.PaymentApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PaymentServiceImpl.class)
class PaymentServiceImplTest {
    @MockitoBean
    private PaymentApi paymentApi;
    @Autowired
    private PaymentServiceImpl paymentService;

    @Test
    void processPayment_shouldNotThrowError_whenPaymentServiceProcessRequest() {
        long totalAmount = 150;
        when(paymentApi.paymentsPost(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(paymentService.processPayment(totalAmount))
                .verifyComplete();
        verify(paymentApi, times(1)).paymentsPost(totalAmount);
    }

    @Test
    void processPayment_shouldThrowError_whenPaymentServiceNotProcessRequest() {
        long totalAmount = 150;
        when(paymentApi.paymentsPost(anyLong()))
                .thenReturn(Mono.error(new WebClientResponseException(400, "Error", null, null, null)));

        StepVerifier.create(paymentService.processPayment(totalAmount))
                .verifyError(WebClientResponseException.class);
    }
}