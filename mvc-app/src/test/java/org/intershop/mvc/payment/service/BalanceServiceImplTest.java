package org.intershop.mvc.payment.service;

import org.intershop.mvc.payment.client.api.BalanceApi;
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
    private BalanceApi balanceApi;
    @Autowired
    private BalanceServiceImpl balanceService;


    @Test
    void getBalance_shouldReturnNonEmpty_whenPaymentServiceAvailable() {
        long userId = 1;
        long balance = 100;
        when(balanceApi.balanceGet(anyLong())).thenReturn(Mono.just(balance));

        StepVerifier.create(balanceService.getBalance(userId))
                .assertNext(balanceResult -> {
                        assertThat(balanceResult.value()).isEqualTo(balance);
                        assertThat(balanceResult.errorMessage()).isNull();
                })
                .verifyComplete();
        verify(balanceApi, times(1)).balanceGet(userId);
    }

    @Test
    void getBalance_shouldReturnEmpty_whenPaymentServiceNotAvailable() {
        long userId = 1;
        when(balanceApi.balanceGet(anyLong())).thenReturn(Mono.error(new RuntimeException("Error")));

         StepVerifier.create(balanceService.getBalance(userId))
                 .assertNext(balanceResult -> {
                         assertThat(balanceResult.value()).isNull();
                         assertThat(balanceResult.errorMessage()).isNotEmpty();
                 })
                 .verifyComplete();
         verify(balanceApi, times(1)).balanceGet(userId);
    }

}