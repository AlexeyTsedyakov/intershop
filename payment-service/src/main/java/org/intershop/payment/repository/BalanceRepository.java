package org.intershop.payment.repository;

import reactor.core.publisher.Mono;

public interface BalanceRepository {
    Mono<Long> getBalance();

    Mono<Void> updateBalance(long balance);
}
