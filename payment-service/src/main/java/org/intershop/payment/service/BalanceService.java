package org.intershop.payment.service;

import reactor.core.publisher.Mono;

public interface BalanceService {
    Mono<Long> getBalance(long userId);

    Mono<Void> updateBalance(long userId, long balance);
}
