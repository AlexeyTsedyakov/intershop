package org.intershop.payment.repository.impl;

import org.intershop.payment.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class BalanceRepositoryFakeImpl implements BalanceRepository {
    @Value("${config.init-balance:0}")
    private long balance;

    @Override
    public Mono<Long> getBalance() {
        return Mono.just(balance);
    }

    @Override
    public Mono<Void> updateBalance(long balance) {
        return Mono.fromRunnable(() -> this.balance = balance);
    }
}
