package org.intershop.payment.service;

import org.intershop.payment.repository.BalanceRepository;
import org.intershop.payment.service.impl.BalanceServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = BalanceServiceImpl.class)
class BalanceServiceImplTest {
    @MockitoBean
    private BalanceRepository balanceRepository;
    @Autowired
    private BalanceServiceImpl balanceService;

    @Test
    void shouldReturnBalance() {
        long balance = 12345;
        when(balanceRepository.getBalance()).thenReturn(Mono.just(balance));

        StepVerifier.create(balanceService.getBalance())
                .assertNext(result -> assertThat(result).isEqualTo(balance))
                .verifyComplete();
        verify(balanceRepository, times(1)).getBalance();
    }

    @Test
    void shouldUpdateBalance_withCorrectValue() {
        long newBalance = 99999;
        when(balanceRepository.updateBalance(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(balanceService.updateBalance(newBalance))
                .verifyComplete();
        verify(balanceRepository, times(1)).updateBalance(newBalance);
    }

    @Test
    void shouldThrowException_whenUpdateBalance_withIncorrectValue() {
        long newBalance = -99999;

        StepVerifier.create(balanceService.updateBalance(newBalance))
                .verifyError(IllegalArgumentException.class);
        verify(balanceRepository, never()).updateBalance(anyLong());
    }
}