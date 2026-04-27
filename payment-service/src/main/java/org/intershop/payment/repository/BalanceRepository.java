package org.intershop.payment.repository;

import reactor.core.publisher.Mono;

public interface BalanceRepository {
    Mono<Long> getBalance(long userId);

    Mono<Void> updateBalance(long userId, long balance);
}
