package org.intershop.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.intershop.payment.model.PaymentRequest;
import org.intershop.payment.service.BalanceService;
import org.intershop.payment.service.PaymentService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final BalanceService balanceService;

    @Override
    public Mono<Void> processPayment(PaymentRequest paymentRequest) {
        long userId = paymentRequest.getUserId();
        long amount = paymentRequest.getAmount();

        return balanceService.getBalance(userId)
                .flatMap(balance -> {
                    if (amount > balance) {
                        return Mono.error(new IllegalArgumentException("Недостаточно средств. Баланс: " + balance));
                    }
                    return balanceService.updateBalance(userId, balance - amount);
                });
    }
}
