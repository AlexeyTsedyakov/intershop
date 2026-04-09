package org.intershop.mvc.payment.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class BalanceServiceIntegrationTest {
    @Autowired
    private BalanceService balanceService;

    @Test
    void getBalance_shouldReturnNonEmpty() {
        StepVerifier.create(balanceService.getBalance())
                .assertNext(balanceOpt ->
                        assertThat(balanceOpt).isNotEmpty())
                .verifyComplete();
    }
}
