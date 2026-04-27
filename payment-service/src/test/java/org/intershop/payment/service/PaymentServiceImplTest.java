package org.intershop.payment.service;

import org.intershop.payment.model.PaymentRequest;
import org.intershop.payment.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;


@SpringBootTest(classes = PaymentServiceImpl.class)
class PaymentServiceImplTest {
    @MockitoBean
    private BalanceService balanceService;
    @Autowired
    private PaymentServiceImpl paymentService;

    @Test
    void shouldProcessPayment_whenAmountValid() {
        long userId = 1;
        long balance = 10000;
        long amount = 5000;
        when(balanceService.getBalance(anyLong())).thenReturn(Mono.just(balance));
        when(balanceService.updateBalance(anyLong(), anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(paymentService.processPayment(new PaymentRequest(userId, amount)))
                .verifyComplete();
        verify(balanceService, times(1)).getBalance(userId);
        verify(balanceService, times(1)).updateBalance(userId, balance - amount);
    }

    @Test
    void shouldThrowException_whenProcessPayment_withIncorrectAmount() {
        long userId = 1;
        long balance = 1000;
        long amount = 5000;
        when(balanceService.getBalance(anyLong())).thenReturn(Mono.just(balance));

        StepVerifier.create(paymentService.processPayment(new PaymentRequest(userId, amount)))
                .verifyError(IllegalArgumentException.class);
        verify(balanceService, times(1)).getBalance(userId);
        verify(balanceService, never()).updateBalance(anyLong(), anyLong());
    }
}