package org.intershop.payment.service;

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
        long balance = 10000;
        long amount = 5000;
        when(balanceService.getBalance()).thenReturn(Mono.just(balance));
        when(balanceService.updateBalance(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(paymentService.processPayment(amount))
                .verifyComplete();
        verify(balanceService, times(1)).getBalance();
        verify(balanceService, times(1)).updateBalance(balance - amount);
    }

    @Test
    void shouldThrowException_whenProcessPayment_withIncorrectAmount() {
        long balance = 1000;
        long amount = 5000;
        when(balanceService.getBalance()).thenReturn(Mono.just(balance));

        StepVerifier.create(paymentService.processPayment(amount))
                .verifyError(IllegalArgumentException.class);
        verify(balanceService, times(1)).getBalance();
        verify(balanceService, never()).updateBalance(anyLong());
    }
}