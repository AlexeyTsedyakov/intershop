package org.intershop.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.intershop.payment.service.BalanceService;
import org.intershop.payment.service.PaymentService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final BalanceService balanceService;

    @Override
    public Mono<Void> processPayment(long amount) {
        return balanceService.getBalance()
                .flatMap(balance -> {
                    if (amount > balance) {
                        return Mono.error(new IllegalArgumentException("Недостаточно средств. Баланс: " + balance));
                    }
                    return balanceService.updateBalance(balance - amount);
                });
    }
}
