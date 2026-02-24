package org.intershop.mvc.payment.service;

import reactor.core.publisher.Mono;

import java.util.Optional;

public interface BalanceService {
    Mono<Optional<Long>> getBalance();
}
