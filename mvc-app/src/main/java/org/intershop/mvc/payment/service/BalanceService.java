package org.intershop.mvc.payment.service;

import org.intershop.mvc.payment.model.BalanceResult;
import reactor.core.publisher.Mono;

public interface BalanceService {
    Mono<BalanceResult> getBalance();
}
