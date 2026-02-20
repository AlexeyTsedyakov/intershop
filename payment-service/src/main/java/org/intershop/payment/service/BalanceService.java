package org.intershop.payment.service;

import reactor.core.publisher.Mono;

public interface BalanceService {
    Mono<Long> getBalance();

    Mono<Void> updateBalance(long balance);
}
